package com.chunjae.nest.domain.paper.controller;

import com.chunjae.nest.domain.paper.dto.SearchKeywordDTO;
import com.chunjae.nest.domain.paper.entity.Paper;
import com.chunjae.nest.domain.paper.service.PaperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public String index(Model model, @ModelAttribute SearchKeywordDTO searchKeywordDTO) {

        System.out.println("==============================================");
        System.out.println("컨트롤러 부분 - " + searchKeywordDTO.toString());
        //List<Paper> papers = paperService.findPapers(); // 전체 조회

        List<Paper> papers = paperService.searchResults(searchKeywordDTO); // 조건에 따른 조회
        model.addAttribute("papers", papers);

        return "pages/paper/index";
    }

    @GetMapping("/upload")
    public String upload() {
        return "forward:/react/paper.html";
    }
}
