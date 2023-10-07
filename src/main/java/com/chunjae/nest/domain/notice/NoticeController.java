package com.chunjae.nest.domain.notice;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NoticeController {
    @GetMapping("/")
    public String index() {
        return "pages/index";
    }
}
