package com.chunjae.nest.domain.question.service;

import com.chunjae.nest.domain.paper.entity.Paper;
import com.chunjae.nest.domain.paper.entity.PaperLog;
import com.chunjae.nest.domain.paper.entity.PaperStatus;
import com.chunjae.nest.domain.paper.repository.PaperLogRepository;
import com.chunjae.nest.domain.paper.service.S3UploadService;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class QuestionService {

    private final S3UploadService s3UploadService;
    private final QuestionRepository questionRepository;
    private final QuestionFileRepository questionFileRepository;
    private final QuestionLogRepository questionLogRepository;
    private final PaperLogRepository paperLogRepository;
    private final UserRepository userRepository;

    @Transactional
    public String uploadQuestionFile(QuestionRequest questionRequest) throws IOException {
        log.info("questionRequest: {}", questionRequest.toString());
        Long id = 1L;

        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("유저를 찾을수 없습니다."));
        MultipartFile multipartFile = questionRequest.getMultipartFile();
        Question question = questionRequest.createQuestion(user);

        String questionUrl = s3UploadService.uploadPaper(multipartFile);
        String questionFileName = s3UploadService.getFileName(questionUrl);

        if (!questionUrl.equals("failed")) {
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
            log.info("paperName: {}", question.getPaper().toString());
            log.info("questionFile:{}, questionLog: {}", questionFile.toString(), questionLog.toString());


            questionFileRepository.save(questionFile);
            questionLogRepository.save(questionLog);

            return "ok";
        }
        return "failed";
    }

    @Transactional
    public String saveQuestion(Long id, String content) {
        Long userId = 1L;
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("유저를 찾을수 없습니다."));
        Question question = questionRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("문제를 찾을 수 없습니다."));

        if (!content.equals("")) {

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

            questionRepository.save(question);
            questionLogRepository.save(questionLog);
            return "ok";
        }
        return "failed";
    }

    @Transactional(readOnly = true)
    public QuestionResponse getQuestionDetail(Long id, int num) {
        return questionRepository.findByPaperIdAndNum(id, num)
                .map(questionData -> QuestionResponse.builder()
                        .num(questionData.getNum())
                        .type(questionData.getType())
                        .content(questionData.getContent())
                        .url(questionData.getQuestionFile().getUrl())
                        .build())
                .orElse(null);
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

    public Page<Paper> searchOCR(SearchKeywordDTO searchKeywordDTO, Pageable pageable) { // OCR 작업중
        return questionRepository.searchOCR(searchKeywordDTO, pageable);
    }

    public Page<Paper> searchOCRDone(SearchKeywordDTO searchKeywordDTO, Pageable pageable) { // OCR 작업 완료
        return questionRepository.searchOCRDone(searchKeywordDTO, pageable);
    }

    public List<Question> searchResults() {
        return questionRepository.findAll();
    }
}
