package com.chunjae.nest.domain.paper.controller;

import com.chunjae.nest.domain.paper.dto.req.PaperRequest;
import com.chunjae.nest.domain.paper.dto.res.PaperResponse;
import com.chunjae.nest.domain.paper.service.PaperService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/paper")
public class PaperApiController {

    private final PaperService paperService;

    @PostMapping("/upload")
    public ResponseEntity<String> saveUploadedPaper(@RequestPart(value = "paperRequest") PaperRequest paperRequest,
                                                    @RequestPart(value = "multipartFile") MultipartFile multipartFile) throws IOException {

        String saveUploadedPaper = paperService.saveUploadedPaper(paperRequest, multipartFile);
        if (saveUploadedPaper.equals("ok")) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<PaperResponse> getPaperDetail(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok().body(paperService.getPaperDetail(id));
    }


}
