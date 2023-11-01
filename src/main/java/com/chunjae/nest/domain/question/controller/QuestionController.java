package com.chunjae.nest.domain.question.controller;

import com.chunjae.nest.domain.paper.dto.SearchKeywordDTO;
import com.chunjae.nest.domain.paper.entity.Paper;
import com.chunjae.nest.domain.question.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/ocr")
public class QuestionController {

    private final QuestionService questionService;

    @GetMapping("")
    public String index(Model model,
                        @ModelAttribute SearchKeywordDTO searchKeywordDTO,
                        @PageableDefault(size=10, sort="id", direction = Sort.Direction.DESC) Pageable pageable) {

        int[] sizes = {10, 30, 50, 100};

        // 검색
        Page<Paper> papers = questionService.searchOCR(searchKeywordDTO, pageable);
        model.addAttribute("papers", papers);
        model.addAttribute("sizes", sizes);
        //model.addAttribute("page", papers);

        // 페이징
        String ocrPageLink = "/ocr?";
        String year = searchKeywordDTO.getYear();
        int month = searchKeywordDTO.getMonth();
        String area = searchKeywordDTO.getArea();
        String subject = searchKeywordDTO.getSubject();
        String option = searchKeywordDTO.getSearchOption();
        String keyword = searchKeywordDTO.getSearchKeyword();
        String category = searchKeywordDTO.getCategory();
        String status = searchKeywordDTO.getPaperStatus();
        String grade = searchKeywordDTO.getGrade();
        int pageSize = pageable.getPageSize();

        if (year != null) ocrPageLink += "year=" + year + "&";
        if (month != 0) ocrPageLink += "month=" + month + "&";
        if (area != null) ocrPageLink += "area=" + area + "&";
        if (subject != null) ocrPageLink += "subject=" + subject + "&";
        if (option != null) ocrPageLink += "searchOption=" + option + "&";
        if (keyword != null) ocrPageLink += "searchKeyword=" + keyword + "&";
        if (category != null) ocrPageLink += "category=" + category + "&";
        if (status != null) ocrPageLink += "paperStatus=" + status + "&";
        if (grade != null) ocrPageLink += "grade=" + grade + "&";
        if (pageSize != 0) ocrPageLink += "size=" + pageSize + "&";

        model.addAttribute("ocrPageLink", ocrPageLink + "page=");

        return "pages/ocr/index";
    }

    @GetMapping("/done")
    public String done(Model model,
                       @ModelAttribute SearchKeywordDTO searchKeywordDTO,
                       @PageableDefault(size=10, sort="id", direction = Sort.Direction.DESC) Pageable pageable) {

        int[] sizes = {10, 30, 50, 100};

        // 검색
        Page<Paper> papers = questionService.searchOCRDone(searchKeywordDTO, pageable);
        model.addAttribute("papers", papers);
        model.addAttribute("sizes", sizes);
        //model.addAttribute("page", papers);

        // 페이징
        String ocrPageLink = "/ocr/done?";
        String year = searchKeywordDTO.getYear();
        int month = searchKeywordDTO.getMonth();
        String area = searchKeywordDTO.getArea();
        String subject = searchKeywordDTO.getSubject();
        String option = searchKeywordDTO.getSearchOption();
        String keyword = searchKeywordDTO.getSearchKeyword();
        String category = searchKeywordDTO.getCategory();
        String status = searchKeywordDTO.getPaperStatus();
        String grade = searchKeywordDTO.getGrade();
        int pageSize = pageable.getPageSize();

        if (year != null) ocrPageLink += "year=" + year + "&";
        if (month != 0) ocrPageLink += "month=" + month + "&";
        if (area != null) ocrPageLink += "area=" + area + "&";
        if (subject != null) ocrPageLink += "subject=" + subject + "&";
        if (option != null) ocrPageLink += "searchOption=" + option + "&";
        if (keyword != null) ocrPageLink += "searchKeyword=" + keyword + "&";
        if (category != null) ocrPageLink += "category=" + category + "&";
        if (status != null) ocrPageLink += "paperStatus=" + status + "&";
        if (grade != null) ocrPageLink += "grade=" + grade + "&";
        if (pageSize != 0) ocrPageLink += "size=" + pageSize + "&";

        model.addAttribute("ocrPageLink", ocrPageLink + "page=");

        return "pages/ocr/done";
    }

    @GetMapping("/transform")
    public String transform() {
        return "forward:/react/ocr.html";
    }
}
