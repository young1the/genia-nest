package com.chunjae.nest.domain.paper;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/paper")
public class PaperController {
    @GetMapping("")
    public String index() {
        return "pages/paper/index";
    }
}
