package com.chunjae.nest.domain.paper.controller;

import com.chunjae.nest.common.excel.ExcelFile;
import com.chunjae.nest.domain.paper.dto.PaperExcelDTO;
import com.chunjae.nest.domain.paper.entity.Paper;
import com.chunjae.nest.domain.paper.service.PaperService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
    public String index(Model model) {
        // 전체 불러오기
        List<Paper> papers = paperService.findPapers();
        model.addAttribute("papers", papers);
        model.addAttribute("paperCount", papers.size());
        // 동적 쿼리 실행하기
        return "pages/paper/index";
    }

    @GetMapping("/upload")
    public String upload() {
        return "forward:/react/paper.html";
    }

    @GetMapping("/download")
    public void download(HttpServletResponse response) {
        List<Paper> papers = paperService.findPapers();
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
