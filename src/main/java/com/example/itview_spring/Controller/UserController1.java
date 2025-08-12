package com.example.itview_spring.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class UserController1 {
    @GetMapping("/user/login")
    public String loginGet() {
        return "member/login";
    }

    @GetMapping("/user/register")
    public String registerGet() {
        return "member/register";
    }
}
