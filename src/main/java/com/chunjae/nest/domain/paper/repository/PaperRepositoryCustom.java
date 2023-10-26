package com.chunjae.nest.domain.paper.repository;

import com.chunjae.nest.domain.paper.dto.SearchKeywordDTO;
import com.chunjae.nest.domain.paper.entity.Paper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PaperRepositoryCustom {
    //List<Paper> findAll(); // 전체 조회

    Page<Paper> searchByWhere(SearchKeywordDTO searchKeywordDTO, Pageable pageable); // 검색 조건에 따른 조회

    //List<SearchPaperDTO> searchByWhere(SearchKeywordDTO searchKeywordDTO); // 검색 조건에 따른 조회

}
