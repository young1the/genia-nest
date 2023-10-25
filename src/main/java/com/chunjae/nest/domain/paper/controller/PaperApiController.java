package com.chunjae.nest.domain.paper.controller;

import com.chunjae.nest.domain.paper.dto.req.PaperRequest;
import com.chunjae.nest.domain.paper.dto.res.PaperResponse;
import com.chunjae.nest.domain.paper.service.PaperService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/paper")
public class PaperApiController {

    private final PaperService paperService;

    @PostMapping("/upload")
    public ResponseEntity<String> saveUploadedPaper(PaperRequest paperRequest) throws IOException {

        String saveUploadedPaper = paperService.saveUploadedPaper(paperRequest);
        if (saveUploadedPaper.equals("ok")) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getPaperUrl(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok().body(paperService.getPaperUrl(id));
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<PaperResponse> getPaperDetail(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok().body(paperService.getPaperDetail(id));
    }

    @PostMapping("/modify/{id}")
    public ResponseEntity<String> updatePaper(@PathVariable(name = "id") Long id,
                                              PaperRequest paperRequest) throws IOException {

        String updatePaper = paperService.updatePaper(id, paperRequest);
        if (updatePaper.equals("ok")) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/remove/{id}")
    public ResponseEntity<String> deletePaper(@PathVariable(name = "id") Long id) {

        String deletedPaper = paperService.deletePaper(id);

        if (deletedPaper.equals("ok")) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

}
