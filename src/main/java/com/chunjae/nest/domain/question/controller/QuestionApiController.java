package com.chunjae.nest.domain.question.controller;


import com.chunjae.nest.domain.question.dto.req.QuestionRequest;
import com.chunjae.nest.domain.question.dto.res.QuestionResponse;
import com.chunjae.nest.domain.question.entity.Question;
import com.chunjae.nest.domain.question.repository.QuestionRepository;
import com.chunjae.nest.domain.question.service.QuestionService;
import com.chunjae.nest.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/question")
public class QuestionApiController {

    private final QuestionService questionService;
    private final QuestionRepository questionRepository;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadQuestionFile(@SessionAttribute(name = "user") User user,
                                                     QuestionRequest questionRequest) throws IOException {
        Optional<Question> optionalQuestion = questionRepository.findByPaperIdAndNum(questionRequest.getPaper().getId(), questionRequest.getNum());
        if (optionalQuestion.isPresent()) {
            return ResponseEntity.ok().body(questionService.updateQuestion(user, questionRequest));
        }
        return ResponseEntity.ok().body(questionService.uploadQuestionFile(user, questionRequest));
    }

    @PostMapping("/save/{id}")
    public ResponseEntity<String> saveQuestion(@SessionAttribute(name = "user") User user,
                                               @PathVariable(name = "id") Long id,
                                               @RequestBody QuestionRequest.SaveRequest saveRequest) {
        if ("ok".equals(questionService.saveQuestion(user, id, saveRequest.getNum(), saveRequest.getContent()))) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<QuestionResponse> getQuestionDetail(@PathVariable(name = "id") Long id,
                                                              @RequestParam(name = "num") int num) {
        QuestionResponse question = questionService.getQuestionDetail(id, num);
        if (question == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(question);
    }

    @PostMapping("/remove/{id}")
    public ResponseEntity<String> deleteQuestion(@SessionAttribute(name = "user") User user,
                                                 @PathVariable(name = "id") Long id,
                                                 @RequestParam(name = "num") int num) {

        questionService.deleteQuestion(user, id, num);
        return ResponseEntity.ok().build();
    }

}
