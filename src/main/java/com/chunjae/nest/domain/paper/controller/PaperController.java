package com.chunjae.nest.domain.paper.controller;

import com.chunjae.nest.domain.paper.entity.Paper;
import com.chunjae.nest.domain.paper.service.PaperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
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

        // 동적 쿼리 실행하기
        return "pages/paper/index";
    }

    @GetMapping("/upload")
    public String upload() {
        return "forward:/react/paper.html";
    }

    @GetMapping("/test")
    public String test() throws NoSuchFieldException {
        Paper paper = new Paper();
        System.out.println("Heelo");
        Class<Paper> clazz = (Class<Paper>) paper.getClass();
        System.out.println(clazz.getDeclaredField("year"));
        for (Field field :clazz.getDeclaredFields()){
            Annotation[] annotations = field.getAnnotations();
            for (Annotation annotation: annotations) {
            }
            System.out.println(field.getName());
        }

        return "forward:/react/paper.html";
    }
}
