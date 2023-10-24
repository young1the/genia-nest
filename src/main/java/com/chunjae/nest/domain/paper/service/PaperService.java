package com.chunjae.nest.domain.paper.service;

import com.chunjae.nest.domain.paper.dto.req.PaperRequest;
import com.chunjae.nest.domain.paper.dto.res.PaperResponse;
import com.chunjae.nest.domain.paper.entity.Paper;
import com.chunjae.nest.domain.paper.entity.PaperFile;
import com.chunjae.nest.domain.paper.entity.PaperLog;
import com.chunjae.nest.domain.paper.entity.PaperStatus;
import com.chunjae.nest.domain.paper.repository.PaperFileRepository;
import com.chunjae.nest.domain.paper.repository.PaperLogRepository;
import com.chunjae.nest.domain.paper.repository.PaperRepository;
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
public class PaperService {

    private final S3UploadService s3UploadService;
    private final PaperRepository paperRepository;
    private final PaperFileRepository paperFileRepository;
    private final PaperLogRepository paperLogRepository;
    private final UserRepository userRepository;


    @Transactional
    public String saveUploadedPaper(PaperRequest paperRequest, MultipartFile multipartFile) throws IOException {
        log.info(" paperRequest:{}", paperRequest.toString());

        Long id = 1L;
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("유저를 찾을수 없습니다."));

        if (isAllowedFileType(multipartFile)) {
            String url = s3UploadService.uploadPaper(multipartFile);
            String fileName = s3UploadService.getFileName(url);

            log.info(" url: {}, fileName: {}", url, fileName);

            if (!url.equals("failed")) {
                Paper paper = paperRequest.createNewPaper(user);
                PaperFile paperFile = PaperFile.builder()
                        .name(fileName)
                        .url(url)
                        .paper(paper)
                        .build();
                PaperLog paperLog = PaperLog.builder()
                        .userId(user.getUserId())
                        .paperUrl(url)
                        .paperName(paperRequest.getName())
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
    public String updatePaper(Long id, PaperRequest paperRequest, MultipartFile multipartFile) throws IOException {
        log.info("id:{}, paperRequest:{}", id, paperRequest.toString());
        Long userId = 1L;
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("유저를 찾을수 없습니다."));
        Paper paper = paperRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("시험지가 없습니다."));
        PaperFile paperFile = paper.getPaperFile();
        paper.paperToUpdate(paperRequest);

        if (paper.getOcrCount() != 0 && (paper.getPaperStatus() == PaperStatus.IN_PROGRESS || paper.getPaperStatus() == PaperStatus.TO_DO)) {
            paperRepository.save(paper);
            return "ok";
        }
        s3UploadService.deletePaper(paper.getPaperFile().getUrl());

        if (isAllowedFileType(multipartFile)) {
            String url = s3UploadService.uploadPaper(multipartFile);
            String fileName = s3UploadService.getFileName(url);
            log.info("url:{}, fileName:{}", url, fileName);

            if (!url.equals("failed")) {
                paperFile.updatePaperFile(fileName, url);
                PaperLog paperLog = PaperLog.builder()
                        .userId(user.getUserId())
                        .paperUrl(url)
                        .paperName(paperRequest.getName())
                        .build();

                paperRepository.save(paper);
                paperFileRepository.save(paperFile);
                paperLogRepository.save(paperLog);

                return "ok";
            }
        }
        return "failed";
    }


    @Transactional
    public String deletePaper(Long id) {
        Paper paper = paperRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("시험지가 없습니다."));
        String url = paper.getPaperFile().getUrl();

        if (0 == paper.getOcrCount()) {
            s3UploadService.deletePaper(url);
            paper.setPaperStatusToDelete();
            paperRepository.save(paper);
            paperFileRepository.delete(paper.getPaperFile());
            return "ok";
        }
        return "failed";
    }

    public boolean isAllowedFileType(MultipartFile multipartFile) {
        String fileName = multipartFile.getOriginalFilename();
        return fileName != null && fileName.toLowerCase().endsWith(".pdf");
    }

}
