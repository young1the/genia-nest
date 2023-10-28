package com.chunjae.nest.domain.question.controller;

import com.chunjae.nest.domain.paper.dto.SearchKeywordDTO;
import com.chunjae.nest.domain.paper.entity.Paper;
import com.chunjae.nest.domain.paper.entity.PaperStatus;
import com.chunjae.nest.domain.paper.service.PaperService;
import com.chunjae.nest.domain.question.entity.Question;
import com.chunjae.nest.domain.question.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/ocr")
public class QuestionController {

    private final PaperService paperService;
    private final QuestionService questionService;

    @GetMapping("")
    public String index(Model model, @ModelAttribute SearchKeywordDTO searchKeywordDTO, Pageable pageable) {

        int pageSize = 10;
        int currentPage = Math.max(0, pageable.getPageNumber()); // 음수 페이지 번호 방지

        // 검색
        Pageable adjustedPageable = PageRequest.of(currentPage, pageSize);
        Page<Paper> papers = paperService.searchResults(searchKeywordDTO, adjustedPageable);
        model.addAttribute("papers", papers);
        model.addAttribute("page", papers);

        // 페이징
        String pageLink = "";
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
        String ocrPageLink = "/ocr?" + pageLink;
        model.addAttribute("ocrPageLink", ocrPageLink);

        List<Question> questionList = questionService.searchResults();
        model.addAttribute("questionList", questionList);

        return "pages/ocr/index";
    }

    @GetMapping("/done")
    public String done(Model model, @ModelAttribute SearchKeywordDTO searchKeywordDTO, Pageable pageable) {

        int pageSize = 10;
        int currentPage = Math.max(0, pageable.getPageNumber()); // 음수 페이지 번호 방지

        // 검색
        Pageable adjustedPageable = PageRequest.of(currentPage, pageSize);
        Page<Paper> papers = paperService.searchResults(searchKeywordDTO, adjustedPageable);
        model.addAttribute("papers", papers);
        model.addAttribute("page", papers);

        // 페이징
        String pageLink = "";
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
        String ocrPageLink = "/ocr/done?" + pageLink;
        model.addAttribute("ocrPageLink", ocrPageLink);

        List<Question> questionList = questionService.searchResults();
        model.addAttribute("questionList", questionList);


        return "pages/ocr/done";
    }

    @GetMapping("/transform")
    public String transform() {
        return "forward:/react/ocr.html";
    }
}
