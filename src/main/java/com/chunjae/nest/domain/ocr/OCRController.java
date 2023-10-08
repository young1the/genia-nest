package com.chunjae.nest.domain.ocr;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ocr")
public class OCRController {
    @GetMapping("")
    public String index() {
        return "pages/ocr/index";
    }

    @GetMapping("/done")
    public String done() {
        return "pages/ocr/done";
    }

    @GetMapping("/transform")
    public String transform() {
        return "forward:/react/ocr.html";
    }
}
