package com.chunjae.nest.domain.paper.service;

import com.chunjae.nest.domain.paper.dto.SearchKeywordDTO;
import com.chunjae.nest.domain.paper.entity.Paper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@SpringBootTest
public class PaperServiceTest {

    @Autowired
    PaperService paperService;

    @Test
    public void test(){

        SearchKeywordDTO keywordDTO = new SearchKeywordDTO();
        keywordDTO.setSubject("기하");
        keywordDTO.setCategory("수능");
        keywordDTO.setArea("수학");
        keywordDTO.setGrade("3");
//        Pageable pageable = new Pageable();

        Page<Paper> list =  paperService.searchResults(keywordDTO, null);
        System.out.println(list);
    }
}