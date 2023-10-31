package com.chunjae.nest.domain.question.service;

import com.chunjae.nest.domain.paper.dto.SearchKeywordDTO;
import com.chunjae.nest.domain.paper.entity.Paper;
import com.chunjae.nest.domain.question.dto.QuestionDTO;
import com.chunjae.nest.domain.question.entity.Question;
import com.chunjae.nest.domain.question.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;

    public Page<Paper> searchOCR(SearchKeywordDTO searchKeywordDTO, Pageable pageable) { // OCR 작업중
        return questionRepository.searchOCR(searchKeywordDTO, pageable);
    }

    public Page<Paper> searchOCRDone(SearchKeywordDTO searchKeywordDTO, Pageable pageable) { // OCR 작업 완료
        return questionRepository.searchOCRDone(searchKeywordDTO, pageable);
    }

    public List<Question> searchResults() {
        return questionRepository.findAll();
    }
}
