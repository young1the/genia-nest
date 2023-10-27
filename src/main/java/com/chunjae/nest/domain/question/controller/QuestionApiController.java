package com.chunjae.nest.domain.question.controller;


import com.chunjae.nest.domain.question.dto.req.QuestionRequest;
import com.chunjae.nest.domain.question.dto.res.QuestionResponse;
import com.chunjae.nest.domain.question.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/question")
public class QuestionApiController {

    private final QuestionService questionService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadQuestionFile(QuestionRequest questionRequest) throws IOException {

        String savedUploadedQuestion = questionService.uploadQuestionFile(questionRequest);

        if (savedUploadedQuestion.equals("ok")) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/save/{id}")
    public ResponseEntity<String> saveQuestion(@PathVariable(value = "id") Long id, String content) {

        String savedQuestion = questionService.saveQuestion(id, content);
        if (savedQuestion.equals("ok")) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<QuestionResponse> getQuestionDetail(@PathVariable(value = "id") Long id) {
        return ResponseEntity.ok().body(questionService.getQuestionDetail(id));
    }

}
