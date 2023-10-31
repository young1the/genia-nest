package com.chunjae.nest.domain.user.controller;

import com.chunjae.nest.domain.paper.entity.Paper;
import com.chunjae.nest.domain.user.dto.AssignmentDTO;
import com.chunjae.nest.domain.user.dto.AssignmentSearchReqDTO;
import com.chunjae.nest.domain.user.service.UserAssignmentService;
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
        model.addAttribute("assignments", assignments);
        model.addAttribute("params", assignmentSearchReqDTO);
        return "pages/assignment/index";

    }
}
