package com.chunjae.nest.domain.paper.controller;

import com.chunjae.nest.domain.paper.dto.SearchKeywordDTO;
import com.chunjae.nest.domain.paper.entity.Paper;
import com.chunjae.nest.domain.paper.service.PaperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Controller
@RequestMapping("/paper")
public class PaperController {

    private final PaperService paperService;
    @Autowired
    public PaperController(PaperService paperService) {
        this.paperService = paperService;
    }

    @GetMapping("")
    public String index(Model model, @ModelAttribute SearchKeywordDTO searchKeywordDTO, Pageable pageable) {

        System.out.println("==============================================");
        System.out.println("컨트롤러 부분 - " + searchKeywordDTO.toString());

        int pageSize = 10; // 페이지 크기
        int currentPage = pageable.getPageNumber();

        if (currentPage < 0) {
            currentPage = 0; // 페이지 번호가 음수이면 0으로 설정
        }

        Pageable adjustedPageable = PageRequest.of(currentPage, pageSize);
        Page<Paper> papers = paperService.searchResults(searchKeywordDTO, adjustedPageable);

        //Page<Paper> papers = paperService.searchResults(searchKeywordDTO, pageable); // 조건에 따른 조회
        model.addAttribute("papers", papers);

        int totalPages = papers.getTotalPages(); // 전체 페이지 수
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("page", papers);

        return "pages/paper/index";
    }

    @GetMapping("/upload")
    public String upload() {
        return "forward:/react/paper.html";
    }
}
