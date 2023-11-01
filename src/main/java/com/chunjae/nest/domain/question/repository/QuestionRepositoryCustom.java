package com.chunjae.nest.domain.question.repository;

import com.chunjae.nest.domain.paper.dto.SearchKeywordDTO;
import com.chunjae.nest.domain.paper.entity.Paper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QuestionRepositoryCustom {

    Page<Paper> searchOCR(SearchKeywordDTO searchKeywordDTO, Pageable pageable); // OCR 작업중

    Page<Paper> searchOCRDone(SearchKeywordDTO searchKeywordDTO, Pageable pageable); // OCR 작업 완료
}
