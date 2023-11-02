package com.chunjae.nest.domain.paper.controller;

import com.chunjae.nest.common.excel.ExcelFile;
import com.chunjae.nest.domain.paper.dto.PaperExcelDTO;
import com.chunjae.nest.domain.paper.dto.SearchKeywordDTO;
import com.chunjae.nest.domain.paper.entity.Paper;
import com.chunjae.nest.domain.paper.service.PaperService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.data.domain.Pageable;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/paper")
public class PaperController implements Serializable {

    private final PaperService paperService;

    @GetMapping("")
    public String index(Model model, HttpSession session,
                        @ModelAttribute SearchKeywordDTO searchKeywordDTO,
                        @PageableDefault(size=10, sort="id", direction = Sort.Direction.DESC) Pageable pageable) {

        int[] sizes = {10, 30, 50, 100};

        // 검색
        Page<Paper> papers = paperService.searchResults(searchKeywordDTO, pageable);
        model.addAttribute("papers", papers);
        model.addAttribute("sizes", sizes);

        // 페이징
        String pageLink = "/paper?";
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

        if (year != null) pageLink += "year=" + year + "&";
        if (month != 0) pageLink += "month=" + month + "&";
        if (area != null) pageLink += "area=" + area + "&";
        if (subject != null) pageLink += "subject=" + subject + "&";
        if (option != null) pageLink += "searchOption=" + option + "&";
        if (keyword != null) pageLink += "searchKeyword=" + keyword + "&";
        if (category != null) pageLink += "category=" + category + "&";
        if (status != null) pageLink += "paperStatus=" + status + "&";
        if (grade != null) pageLink += "grade=" + grade + "&";
        if (pageSize != 0) pageLink += "size=" + pageSize + "&";

        model.addAttribute("pageLink", pageLink + "page=");

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
