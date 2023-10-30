package com.chunjae.nest.domain.paper.controller;

import com.chunjae.nest.domain.paper.dto.SearchKeywordDTO;
import com.chunjae.nest.domain.paper.entity.Paper;
import com.chunjae.nest.domain.paper.service.PaperService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.data.domain.Pageable;

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

        int pageSize = 10;
        int currentPage = pageable.getPageNumber();

        if (currentPage < 0) {
            currentPage = 0; // 페이지 번호가 음수이면 0으로 설정
        }

        // 검색
        Pageable adjustedPageable = PageRequest.of(currentPage, pageSize);
        Page<Paper> papers = paperService.searchResults(searchKeywordDTO, adjustedPageable);
        model.addAttribute("papers", papers);
        model.addAttribute("page", papers);

        // 페이징
        String pageLink = "/paper?";
        if (searchKeywordDTO.getYear() != null) {
            pageLink += "year=" + searchKeywordDTO.getYear() + "&";
        }
        if (searchKeywordDTO.getMonth() != 0) {
            pageLink += "month=" + searchKeywordDTO.getMonth() + "&";
        }
        if (searchKeywordDTO.getArea() != null) {
            pageLink += "area=" + searchKeywordDTO.getArea() + "&";
        }
        if (searchKeywordDTO.getSubject() != null) {
            pageLink += "subject=" + searchKeywordDTO.getSubject() + "&";
        }
        pageLink += "page=";
        model.addAttribute("pageLink", pageLink);

        return "pages/paper/index";
    }

    @GetMapping("/upload")
    public String upload() {
        return "forward:/react/paper.html";
    }

}
