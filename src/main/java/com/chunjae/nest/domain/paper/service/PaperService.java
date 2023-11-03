package com.chunjae.nest.domain.paper.service;

import com.chunjae.nest.domain.paper.dto.SearchKeywordDTO;
import com.chunjae.nest.domain.paper.dto.req.PaperAssignmentRequest;
import com.chunjae.nest.domain.paper.dto.req.PaperRequest;
import com.chunjae.nest.domain.paper.dto.res.PaperResponse;
import com.chunjae.nest.domain.paper.entity.*;
import com.chunjae.nest.domain.paper.repository.PaperAssignmentRepository;
import com.chunjae.nest.domain.paper.repository.PaperFileRepository;
import com.chunjae.nest.domain.paper.repository.PaperLogRepository;
import com.chunjae.nest.domain.paper.repository.PaperRepository;
import com.chunjae.nest.domain.user.entity.RoleStatus;
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
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaperService {

    private final S3UploadService s3UploadService;
    private final PaperRepository paperRepository;
    private final PaperAssignmentRepository paperAssignmentRepository;
    private final PaperFileRepository paperFileRepository;
    private final PaperLogRepository paperLogRepository;
    private final UserRepository userRepository;


    @Transactional
    public String saveUploadedPaper(User user, PaperRequest paperRequest) throws IOException {
        log.info("paperRequest:{}", paperRequest.toString());
        log.info("user :{}", user.getRole().toString());
        User userData = userRepository.findById(user.getId()).orElseThrow(() -> new IllegalArgumentException("유저를 찾을수 없습니다."));
//       if (!isAllowedUser(userData)){
//           return "failed";
//       }

        MultipartFile multipartFile = paperRequest.getMultipartFile();
        if (multipartFile == null) {
            Paper paper = paperRequest.createPaper(userData);
            PaperFile paperFile = PaperFile.builder()
                    .name("")
                    .url("")
                    .paper(paper)
                    .build();
            PaperLog paperLog = PaperLog.builder()
                    .userId(user.getUserId())
                    .paperUrl("")
                    .paperName(paperRequest.getName())
                    .paperStatus(PaperStatus.TO_DO)
                    .build();
            paperRepository.save(paper);
            paperFileRepository.save(paperFile);
            paperLogRepository.save(paperLog);
            return "ok";
        }
        if (isAllowedFileType(multipartFile)) {
            String url = s3UploadService.uploadPaper(multipartFile);
            String fileName = s3UploadService.getFileName(url);

            log.info(" url: {}, fileName: {}", url, fileName);

            if (!"failed".equals(url)) {
                Paper paper = paperRequest.createPaper(user);
                PaperFile paperFile = PaperFile.builder()
                        .name(fileName)
                        .url(url)
                        .paper(paper)
                        .build();
                PaperLog paperLog = PaperLog.builder()
                        .userId(user.getUserId())
                        .paperUrl(url)
                        .paperName(paperRequest.getName())
                        .paperStatus(PaperStatus.TO_DO)
                        .build();

                paperRepository.save(paper);
                paperFileRepository.save(paperFile);
                paperLogRepository.save(paperLog);

                return "ok";
            }
        }
        return "failed";
    }

    @Transactional(readOnly = true)
    public String getPaperUrl(Long id) {
        Paper paper = paperRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("시험지가 없습니다."));
        return paper.getPaperFile().getUrl();
    }

    @Transactional(readOnly = true)
    public PaperResponse getPaperDetail(Long id) {
        Paper paper = paperRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("시험지가 없습니다."));
        return PaperResponse.builder()
                .year(paper.getYear())
                .month(paper.getMonth())
                .grade(paper.getGrade())
                .name(paper.getName())
                .totalCount(paper.getTotalCount())
                .category(paper.getCategory())
                .area(paper.getArea())
                .subject(paper.getSubject())
                .url(paper.getPaperFile().getUrl())
                .build();
    }

    @Transactional
    public String updatePaper(User user, Long id, PaperRequest paperRequest) throws IOException {
        log.info("id:{}, paperRequest:{}", id, paperRequest.toString());
        User userData = userRepository.findById(user.getId()).orElseThrow(() -> new IllegalArgumentException("유저를 찾을수 없습니다."));
        Paper paper = paperRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("시험지가 없습니다."));
        validateUserAndPaper(userData, paper);

        if (!isAllowedUser(userData)){
            return "failed";
        }
        String originUrl = paper.getPaperFile().getUrl();
        PaperFile paperFile = paper.getPaperFile();
        paper.paperToUpdate(paperRequest);
        MultipartFile multipartFile = paperRequest.getMultipartFile();

        if (paper.getOcrCount() != 0 && (paper.getPaperStatus() == PaperStatus.IN_PROGRESS || paper.getPaperStatus() == PaperStatus.TO_DO)) {
            return "ok";
        }
        if (multipartFile == null) {
            paperFile.updatePaperFile("", "");
            s3UploadService.deletePaper(originUrl);
            return "ok";
        }

        String url = s3UploadService.uploadPaper(multipartFile);
        String fileName = s3UploadService.getFileName(url);
        try {

            if (isAllowedFileType(multipartFile) && !multipartFile.isEmpty()) {
                log.info("url:{}, fileName:{}", url, fileName);

                if (!"failed".equals(url)) {
                    paperFile.updatePaperFile(fileName, url);
                    s3UploadService.deletePaper(originUrl);
                    return "ok";
                }
            }
            return "failed";
        } catch (Exception e) {
            s3UploadService.deletePaper(url);
            return "failed";
        }
    }

    @Transactional
    public String deletePaper(User user, Long id) {
        User userData = userRepository.findById(user.getId()).orElseThrow(() -> new IllegalArgumentException("유저를 찾을수 없습니다."));
        Paper paper = paperRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("시험지가 없습니다."));
        String url = paper.getPaperFile().getUrl();
        validateUserAndPaper(userData, paper);
        if (!isAllowedUser(userData)){
            return "failed";
        }

        if (0 == paper.getOcrCount()) {
            s3UploadService.deletePaper(url);
            paper.setPaperStatus(PaperStatus.DELETED);
            paperRepository.save(paper);
            paperFileRepository.delete(paper.getPaperFile());
            PaperLog paperLog = PaperLog.builder()
                    .userId(paper.getUser().getUserId())
                    .paperName(paper.getName())
                    .paperUrl("")
                    .paperStatus(PaperStatus.DELETED)
                    .build();
            paperLogRepository.save(paperLog);
            return "ok";
        }
        return "failed";
    }

    @Transactional
    public String assignTaskPaper(PaperAssignmentRequest paperAssignmentRequest) {

        User user = userRepository.findById(paperAssignmentRequest.getUserId()).orElseThrow(() -> new IllegalArgumentException("유저를 찾을수 없습니다."));
        Paper paper = paperRepository.findById(paperAssignmentRequest.getPaperId()).orElseThrow(() -> new IllegalArgumentException("시험지가 없습니다."));
        Optional<PaperAssignment> assignmentUser = paperAssignmentRepository.findByUserAndPaper(user, paper);
        if (assignmentUser.isPresent()) {
            PaperAssignment paperAssignment = assignmentUser.get();
            paperAssignment.updatePaperAssignmentStatus(PaperAssignmentStatus.TO_DO);
            return "ok";
        }
        PaperAssignment paperAssignment = PaperAssignment.builder()
                .user(user)
                .paper(paper)
                .paperAssignmentStatus(PaperAssignmentStatus.TO_DO)
                .build();
        paperAssignmentRepository.save(paperAssignment);

        return "ok";
    }

    @Transactional
    public String unassignTaskPaper(PaperAssignmentRequest paperAssignmentRequest) {

        User user = userRepository.findById(paperAssignmentRequest.getUserId()).orElseThrow(() -> new IllegalArgumentException("유저를 찾을수 없습니다."));
        Paper paper = paperRepository.findById(paperAssignmentRequest.getPaperId()).orElseThrow(() -> new IllegalArgumentException("시험지가 없습니다."));
        PaperAssignment paperAssignment = paperAssignmentRepository.findByUserAndPaper(user, paper).orElseThrow(() -> new IllegalArgumentException("지정된 작업자가 없습니다."));
        paperAssignment.updatePaperAssignmentStatus(PaperAssignmentStatus.CANCELLED);
        return "ok";
    }

    public void validateUserAndPaper(User user, Paper paper) {
        if (!Objects.equals(user.getId(), paper.getUser().getId())) {
            throw new IllegalArgumentException("유저와 시험지의 등록자가 일치하지 않습니다.");
        }
    }

    public boolean isAllowedFileType(MultipartFile multipartFile) {
        String fileName = multipartFile.getOriginalFilename();
        return fileName != null && fileName.toLowerCase().endsWith(".pdf");
    }

    public boolean isAllowedUser(User user){
        if (!"문제담당자".equals(user.getRole().getRole()) && (user.getRole().getRoleStatus() == RoleStatus.TERMINATED ||
                user.getRole().getRoleStatus() == RoleStatus.CANCELLED)){
            return false;
        }
        return true;
    }

    public Page<Paper> searchResults(SearchKeywordDTO searchKeywordDTO, Pageable pageable) {
        return paperRepository.searchByWhere(searchKeywordDTO, pageable);
    }


}
