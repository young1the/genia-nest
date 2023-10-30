package com.chunjae.nest.domain.user.controller;

import com.chunjae.nest.domain.user.dto.AssignmentDTO;
import com.chunjae.nest.domain.user.dto.AssignmentSearchReqDTO;
import com.chunjae.nest.domain.user.service.UserAssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/user/management/assignment")
public class UserAssignmentController {
    @Autowired
    private final UserAssignmentService userAssignmentService;
    @GetMapping("")
    public String index(Model model, @ModelAttribute AssignmentSearchReqDTO assignSearchDTO, Pageable pageable) {
        System.out.println(assignSearchDTO);
        int pageSize = 10;
        int currentPage = pageable.getPageNumber();
        if (currentPage < 0) {
            currentPage = 0;
        }
        Pageable adjustedPageable = PageRequest.of(currentPage, pageSize);

        Page<AssignmentDTO> assignments = userAssignmentService.searchResults();
        model.addAttribute("assignments", assignments);
        return "pages/assignment/index";

    }
}
