package com.chunjae.nest.domain.question.controller;

import com.chunjae.nest.domain.question.dto.*;
import com.chunjae.nest.domain.question.service.OCRService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/ocr")
@Slf4j
public class OCRController {
    @Value("${nest.ocr.math.id}")
    private String MATH_API_ID;
    @Value("${nest.ocr.math.key}")
    private String MATH_API_KEY;
    @Value("${nest.ocr.text.key}")
    private String TEXT_API_KEY;

    @Autowired
    private OCRService ocrService;

    public OCRController(OCRService ocrService) {
        this.ocrService = ocrService;
    }

    @PostMapping("/math")
    public ResponseEntity<OCRMathResDTO> math(@RequestBody OCRMathReqDTO reqDTO) {
        try {
            OCRMathResDTO response = ocrService.transMath(reqDTO);
            return ResponseEntity.ok().body(response);
        } catch (Exception exception) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/text")
    public ResponseEntity<OCRTextResDTO> text(@RequestBody OCRTextReqDTO reqDTO) {
        try {
            System.out.println(reqDTO.getSrc());
            OCRTextResDTO response = ocrService.transText(reqDTO);
            System.out.println(response.getText() + response.getRequest_id());
            return ResponseEntity.ok().body(response);
        } catch (Exception exception) {
            return ResponseEntity.badRequest().build();
        }
    }
}
