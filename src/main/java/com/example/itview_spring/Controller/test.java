package com.example.itview_spring.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class test {
    
    @GetMapping
    public String index() {
        return "index";
    }
}
