package com.chunjae.nest.domain.user.controller;

import com.chunjae.nest.common.excel.ExcelFile;
import com.chunjae.nest.domain.paper.dto.PaperExcelDTO;
import com.chunjae.nest.domain.paper.dto.SearchKeywordDTO;
import com.chunjae.nest.domain.paper.entity.Paper;
import com.chunjae.nest.domain.user.dto.AssignmentDTO;
import com.chunjae.nest.domain.user.dto.AssignmentSearchReqDTO;
import com.chunjae.nest.domain.user.service.UserAssignmentService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RequestParam;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user/management/assignment")
public class UserAssignmentController {
    @Autowired
    private final UserAssignmentService userAssignmentService;

    @GetMapping("")
    public String index(Model model, @ModelAttribute AssignmentSearchReqDTO assignmentSearchReqDTO, @PageableDefault(size=10, sort="id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<AssignmentDTO> assignments = userAssignmentService.searchResults(assignmentSearchReqDTO, pageable);
        int[] sizes = {10,30,100};
        model.addAttribute("assignments", assignments);
        model.addAttribute("params", assignmentSearchReqDTO);
        model.addAttribute("sizes", sizes);
        return "pages/assignment/index";

    }

    @GetMapping("/download")
    public void download(@ModelAttribute AssignmentSearchReqDTO assignmentSearchReqDTO, HttpServletResponse response) {
        Pageable adjustedPageable = PageRequest.of(0, Integer.MAX_VALUE);
        Page<AssignmentDTO> assignments = userAssignmentService.searchResults(assignmentSearchReqDTO, adjustedPageable);
        try {
            ExcelFile<AssignmentDTO> paperExcelFile = new ExcelFile<>(assignments.toList(), AssignmentDTO.class);
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
