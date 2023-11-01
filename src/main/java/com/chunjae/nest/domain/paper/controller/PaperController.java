package com.chunjae.nest.domain.paper.controller;

import com.chunjae.nest.common.excel.ExcelFile;
import com.chunjae.nest.domain.paper.dto.PaperExcelDTO;
import com.chunjae.nest.domain.paper.dto.SearchKeywordDTO;
import com.chunjae.nest.domain.paper.entity.Paper;
import com.chunjae.nest.domain.paper.service.PaperService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.data.domain.Pageable;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/paper")
public class PaperController {

    private final PaperService paperService;

    @GetMapping("")
    public String index(Model model, @ModelAttribute SearchKeywordDTO searchKeywordDTO, Pageable pageable) {

        int pageSize = 10;
        int currentPage = Math.max(0, pageable.getPageNumber()); // 음수 페이지 번호 방지
        // 검색
        Pageable adjustedPageable = PageRequest.of(currentPage, pageSize);
        Page<Paper> papers = paperService.searchResults(searchKeywordDTO, adjustedPageable);
        model.addAttribute("papers", papers);

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

    @GetMapping("/download")
    public void download(@ModelAttribute SearchKeywordDTO searchKeywordDTO, HttpServletResponse response) {
        Pageable adjustedPageable = PageRequest.of(0, Integer.MAX_VALUE);
        Page<Paper> papers = paperService.searchResults(searchKeywordDTO, adjustedPageable);
        System.out.println(searchKeywordDTO);
        try {
            List<PaperExcelDTO> cells = papers.stream().map(paper -> PaperExcelDTO.
                    builder()
                    .id(paper.getId())
                    .year(paper.getYear())
                    .month(paper.getMonth())
                    .category(paper.getCategory())
                    .grade(paper.getGrade())
                    .area(paper.getArea())
                    .subject(paper.getSubject())
                    .name(paper.getName())
                    .userId(paper.getUser().getName())
                    .createdAt(paper.getCreatedAt())
                    .paperStatus(paper.getPaperStatus())
                    .build()).toList();
            ExcelFile<PaperExcelDTO> paperExcelFile = new ExcelFile<>(cells, PaperExcelDTO.class);
            response.setHeader("Content-Disposition", "attachment;filename=" +
                    URLEncoder.encode(System.currentTimeMillis() + ".xlsx", StandardCharsets.UTF_8) + ";");
            OutputStream outputStream = response.getOutputStream();
            paperExcelFile.write(outputStream);
            outputStream.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
