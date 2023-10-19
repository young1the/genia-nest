package com.chunjae.nest.domain.paper.controller;

import com.chunjae.nest.domain.paper.dto.req.PaperRequest;
import com.chunjae.nest.domain.paper.service.PaperService;
import com.chunjae.nest.domain.paper.service.S3UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/paper")
public class PaperApiController {

    private final S3UploadService s3UploadService;
    private final PaperService paperService;

    @PostMapping("upload")
    public ResponseEntity<String> paperUploaded(@RequestPart(value = "paperRequest") PaperRequest paperRequest,
                                                @RequestPart(value = "multipartFile") MultipartFile multipartFile) throws IOException {

        String saveUploadedPaper = paperService.saveUploadedPaper(paperRequest, multipartFile);
        if (saveUploadedPaper.equals("ok")) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

}
