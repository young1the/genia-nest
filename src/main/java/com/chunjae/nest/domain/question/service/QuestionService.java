package com.chunjae.nest.domain.question.service;

import com.chunjae.nest.domain.question.entity.Question;
import com.chunjae.nest.domain.question.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;

    public List<Question> searchResults() {
        return questionRepository.findAll();
    }
}
