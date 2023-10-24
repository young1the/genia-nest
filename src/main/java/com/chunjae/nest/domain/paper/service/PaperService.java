package com.chunjae.nest.domain.paper.service;

import com.chunjae.nest.domain.paper.dto.SearchKeywordDTO;
import com.chunjae.nest.domain.paper.entity.Paper;
import com.chunjae.nest.domain.paper.repository.PaperRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaperService {
    private final PaperRepository paperRepository;

    @Autowired
    public PaperService(PaperRepository paperRepository) {
        this.paperRepository = paperRepository;
    }

    public List<Paper> findPapers() { // 사용 x. 임시
        return paperRepository.findAll(Sort.by(Sort.Order.desc("id")));
    }

    public List<Paper> searchResults(SearchKeywordDTO searchKeywordDTO) {
      return paperRepository.searchByWhere(searchKeywordDTO);
    }

}
