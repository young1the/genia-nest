package com.chunjae.nest.domain.paper.repository;

import com.chunjae.nest.domain.paper.dto.SearchKeywordDTO;
import com.chunjae.nest.domain.paper.entity.Paper;

import java.util.List;

public interface PaperRepositoryCustom {
    //List<Paper> findAll(); // 전체 조회

    List<Paper> searchByWhere(SearchKeywordDTO searchKeywordDTO); // 검색 조건에 따른 조회

    //List<SearchPaperDTO> searchByWhere(SearchKeywordDTO searchKeywordDTO); // 검색 조건에 따른 조회

}
