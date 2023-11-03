package com.chunjae.nest.domain.question.service;

import com.chunjae.nest.domain.paper.dto.SearchKeywordDTO;
import com.chunjae.nest.domain.paper.entity.Paper;
import com.chunjae.nest.domain.paper.entity.PaperAssignmentStatus;
import com.chunjae.nest.domain.paper.entity.PaperLog;
import com.chunjae.nest.domain.paper.entity.PaperStatus;
import com.chunjae.nest.domain.paper.repository.PaperLogRepository;
import com.chunjae.nest.domain.paper.service.S3UploadService;
import com.chunjae.nest.domain.question.dto.OCRMathReqDTO;
import com.chunjae.nest.domain.question.dto.OCRMathResDTO;
import com.chunjae.nest.domain.question.dto.OCRTextReqDTO;
import com.chunjae.nest.domain.question.dto.OCRTextResDTO;
import com.chunjae.nest.domain.question.dto.req.QuestionRequest;
import com.chunjae.nest.domain.question.dto.res.QuestionResponse;
import com.chunjae.nest.domain.question.entity.Question;
import com.chunjae.nest.domain.question.entity.QuestionFile;
import com.chunjae.nest.domain.question.entity.QuestionLog;
import com.chunjae.nest.domain.question.entity.QuestionStatus;
import com.chunjae.nest.domain.question.repository.QuestionFileRepository;
import com.chunjae.nest.domain.question.repository.QuestionLogRepository;
import com.chunjae.nest.domain.question.repository.QuestionRepository;
import com.chunjae.nest.domain.user.entity.User;
import com.chunjae.nest.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class QuestionService {

    private final S3UploadService s3UploadService;
    private final OCRService ocrService;
    private final QuestionRepository questionRepository;
    private final QuestionFileRepository questionFileRepository;
    private final QuestionLogRepository questionLogRepository;
    private final PaperLogRepository paperLogRepository;
    private final UserRepository userRepository;

    @Transactional
    public String uploadQuestionFile(User user, QuestionRequest questionRequest) throws IOException {
        log.info("questionRequest: {}", questionRequest.toString());

        User userData = userRepository.findById(user.getId()).orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
        if (!isAssignedUser(userData)) {
            throw new IllegalArgumentException("작업 지정된 유저가 아닙니다.");
        }

        Question question = questionRequest.createQuestion(user);
        MultipartFile multipartFile = questionRequest.getMultipartFile();
        String numExpression = questionRequest.getNumExpression();
        String questionUrl = s3UploadService.uploadPaper(multipartFile);
        String questionFileName = s3UploadService.getFileName(questionUrl);


        try {
            if (!"failed".equals(questionUrl)) {

                String result = performOCR(numExpression, questionUrl);
                question.updateQuestionContent(result);
                questionRepository.save(question);

                QuestionFile questionFile = QuestionFile.builder()
                        .name(questionFileName)
                        .url(questionUrl)
                        .question(question)
                        .build();

                QuestionLog questionLog = QuestionLog.builder()
                        .userId(user.getUserId())
                        .paperName(question.getPaper().getName())
                        .questionUrl(questionUrl)
                        .questionNum(question.getNum())
                        .questionStatus(QuestionStatus.BEFORE)
                        .build();
                log.info("questionFile:{}, questionLog: {}", questionFile.toString(), questionLog.toString());

                questionFileRepository.save(questionFile);
                questionLogRepository.save(questionLog);
                return result;
            }
        } catch (Exception e) {
            s3UploadService.deletePaper(questionUrl);
            e.printStackTrace();
        }
        return "failed";
    }


    @Transactional
    public String saveQuestion(User user, Long id, int num, String content) {
        User userData = userRepository.findById(user.getId()).orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
        Question question = questionRepository.findByPaperIdAndNum(id, num).orElseThrow(() -> new IllegalArgumentException("문제를 찾을 수 없습니다."));
        if (!isAssignedUser(userData)) {
            throw new IllegalArgumentException("작업 지정된 유저가 아닙니다.");
        }

        if (!"".equals(content)) {

            question.updateQuestionContent(content);
            question.updateQuestionStatus(QuestionStatus.COMPLETED);

            Paper paper = question.getPaper();
            paper.setOcrCount(getCompletedQuestionCount(paper));
            updatePaperStatusAndLog(paper);
            QuestionLog questionLog = QuestionLog.builder()
                    .userId(user.getUserId())
                    .paperName(question.getPaper().getName())
                    .questionUrl(question.getQuestionFile().getUrl())
                    .questionNum(question.getNum())
                    .questionStatus(QuestionStatus.COMPLETED)
                    .build();

            questionLogRepository.save(questionLog);
            return "ok";
        }
        return "failed";
    }

    @Transactional(readOnly = true)
    public QuestionResponse getQuestionDetail(Long id, int num) {
        return questionRepository.findByPaperIdAndNum(id, num)
                .map(questionData -> {
                    if (questionData.getQuestionStatus() == QuestionStatus.DELETED) return null;
                    return QuestionResponse.builder()
                            .id(questionData.getId())
                            .num(questionData.getNum())
                            .type(questionData.getType())
                            .content(questionData.getContent())
                            .url(questionData.getQuestionFile().getUrl())
                            .build();
                })
                .orElse(null);
    }

    @Transactional
    public String updateQuestion(User user, QuestionRequest questionRequest) throws IOException {
        User userData = userRepository.findById(user.getId()).orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
        Question question = questionRepository.findByPaperIdAndNum(questionRequest.getPaper().getId(), questionRequest.getNum()).orElseThrow(() -> new IllegalArgumentException("문제를 찾을 수 없습니다."));
        if (!isAssignedUser(userData)) {
            throw new IllegalArgumentException("작업 지정된 유저가 아닙니다.");
        }

        QuestionFile questionFile = question.getQuestionFile();
        String numExpression = questionRequest.getNumExpression();
        MultipartFile multipartFile = questionRequest.getMultipartFile();

        String questionUrl = s3UploadService.uploadPaper(multipartFile);
        String fileName = s3UploadService.getFileName(questionUrl);
        try {


            String result = performOCR(numExpression, questionUrl);
            questionFile.updateQuestionFile(fileName, questionUrl);
            question.updateQuestionContent(result);
            question.updateType(questionRequest.getType());
            question.updateQuestionStatus(QuestionStatus.BEFORE);
            s3UploadService.deletePaper(questionFile.getUrl());
            return result;
        } catch (Exception e) {
            log.info("e:{}", e.getMessage());
            s3UploadService.deletePaper(questionUrl);
            return "failed";
        }
    }

    @Transactional
    public void deleteQuestion(User user, Long id, int num) {
        User userData = userRepository.findById(user.getId()).orElseThrow(() -> new IllegalArgumentException("유저를 찾을수 없습니다."));
        Question question = questionRepository.findByPaperIdAndNum(id, num).orElseThrow(() -> new IllegalArgumentException("문제를 찾을 수 없습니다."));
        if (!isAssignedUser(userData)) {
            throw new IllegalArgumentException("작업 지정된 유저가 아닙니다.");
        }
        question.updateQuestionContent("");
        question.updateQuestionStatus(QuestionStatus.DELETED);
        QuestionFile questionFile = question.getQuestionFile();
        questionFile.updateQuestionFile("", "");
        s3UploadService.deletePaper(questionFile.getUrl());
        QuestionLog questionLog = QuestionLog.builder()
                .userId(userData.getUserId())
                .questionNum(question.getNum())
                .questionUrl(questionFile.getUrl())
                .paperName(question.getPaper().getName())
                .questionStatus(QuestionStatus.DELETED)
                .build();
        questionLogRepository.save(questionLog);
    }

    public boolean isAssignedUser(User user) {
        return user.getPaperAssignments()
                .stream()
                .anyMatch(paper -> paper.getUser().getId().equals(user.getId()) && paper.getPaperAssignmentStatus() == PaperAssignmentStatus.TO_DO);
    }

    public String performOCR(String numExpression, String questionUrl) {
        if ("Y".equalsIgnoreCase(numExpression)) {
            OCRMathReqDTO ocrMathReqDTO = new OCRMathReqDTO(questionUrl);
            OCRMathResDTO ocrResDTO = ocrService.transMath(ocrMathReqDTO);
            return ocrResDTO.getText();
        }
        if ("N".equalsIgnoreCase(numExpression)) {
            OCRTextReqDTO ocrTextReqDTO = new OCRTextReqDTO(questionUrl);
            OCRTextResDTO ocrTextResDTO = ocrService.transText(ocrTextReqDTO);
            return ocrTextResDTO.getText();
        }
        return "failed";
    }

    public void updatePaperStatusAndLog(Paper paper) {
        if (paper.getQuestions().stream().anyMatch(q -> q.getQuestionStatus() == QuestionStatus.COMPLETED)) {
            paper.updatePaperStatus(PaperStatus.IN_PROGRESS);
            paperLogRepository.save(PaperLog.builder()
                    .userId(paper.getUser().getUserId())
                    .paperUrl(paper.getPaperFile().getUrl())
                    .paperName(paper.getName())
                    .paperStatus(PaperStatus.IN_PROGRESS)
                    .build());
        }
        if (paper.getTotalCount() == paper.getOcrCount()) {
            paper.updatePaperStatus(PaperStatus.DONE);
            paperLogRepository.save(PaperLog.builder()
                    .userId(paper.getUser().getUserId())
                    .paperUrl(paper.getPaperFile().getUrl())
                    .paperName(paper.getName())
                    .paperStatus(PaperStatus.DONE)
                    .build());
        }
    }

    public int getCompletedQuestionCount(Paper paper) {
        return (int) paper.getQuestions()
                .stream()
                .filter(q -> q.getQuestionStatus() == QuestionStatus.COMPLETED)
                .count();
    }

    public Page<Paper> searchOCR(SearchKeywordDTO searchKeywordDTO, Pageable pageable) { // OCR 작업중
        return questionRepository.searchOCR(searchKeywordDTO, pageable);
    }

    public Page<Paper> searchOCRDone(SearchKeywordDTO searchKeywordDTO, Pageable pageable) { // OCR 작업 완료
        return questionRepository.searchOCRDone(searchKeywordDTO, pageable);
    }

}
