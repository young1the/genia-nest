package com.chunjae.nest.domain.paper.repository;

import com.chunjae.nest.domain.paper.dto.SearchKeywordDTO;
import com.chunjae.nest.domain.paper.entity.Paper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PaperRepositoryCustom {

    Page<Paper> searchByWhere(SearchKeywordDTO searchKeywordDTO, Pageable pageable); // 검색 조건에 따른 조회

}
