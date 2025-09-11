package com.example.itview_spring.Controller.User;

import com.example.itview_spring.DTO.AdminUserDTO;
import com.example.itview_spring.DTO.PersonDTO;
import com.example.itview_spring.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    // 유저 목록 조회
    @GetMapping("/user/list")
    public String list(
            @PageableDefault(page = 0, size = 10) Pageable pageable,
            @RequestParam(required = false) String keyword,
            Model model) {

        // 1. 서비스 계층을 호출하여 데이터 조회
        Page<AdminUserDTO> userPage = userService.list(pageable, keyword);

        // 2. 조회된 Page 객체를 모델에 담아 뷰로 전달
        model.addAttribute("userPage", userPage);

        // 3. 페이지네이션 처리를 위한 정보도 모델에 담아 전달
        int startPage = Math.max(1, userPage.getPageable().getPageNumber() - 4);
        int endPage = Math.min(userPage.getTotalPages(), userPage.getPageable().getPageNumber() + 4);

        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        // 4. keyword 값을 모델에 추가하여 뷰로 전달하는 핵심 코드
        model.addAttribute("keyword", keyword);
        model.addAttribute("baseUrl", "/user/list");


        // 5. 뷰의 이름 반환
        return "user/list";
    }
}
