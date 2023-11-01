package com.chunjae.nest.domain.question.controller;


import com.chunjae.nest.domain.question.dto.req.QuestionRequest;
import com.chunjae.nest.domain.question.dto.res.QuestionResponse;
import com.chunjae.nest.domain.question.service.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/question")
public class QuestionApiController {

    private final QuestionService questionService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadQuestionFile(QuestionRequest questionRequest) throws IOException {
        return ResponseEntity.ok().body(questionService.uploadQuestionFile(questionRequest));
    }

    @PostMapping("/save/{id}")
    public ResponseEntity<String> saveQuestion(@PathVariable(name = "id") Long id, String content) {
        if ("ok".equals(questionService.saveQuestion(id, content))) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<QuestionResponse> getQuestionDetail(@PathVariable(name = "id") Long id,
                                                              @RequestParam(name = "num") int num) {
        return ResponseEntity.ok().body(questionService.getQuestionDetail(id, num));
    }

    @PostMapping("/remove/{id}")
    public ResponseEntity<String> deleteQuestion(@PathVariable(name = "id") Long id,
                                                 @RequestParam(name = "num") int num) {

        questionService.deleteQuestion(id, num);
        return ResponseEntity.ok().build();
    }

}
