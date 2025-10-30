package com.example.itview_spring.Controller.Admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class AdminController {
    // 관리자 로그인 페이지를 렌더링
    @GetMapping("/admin/login")
    public String loginPage() {
        return "admin/login"; // src/main/resources/templates/admin/login.html 파일로 이동
    }

    // 관리자 회원가입 페이지 (필요하다면)
    @GetMapping("/admin/register")
    public String registerPage() {
        return "admin/register"; // src/main/resources/templates/admin/register.html 파일로 이동
    }
}
