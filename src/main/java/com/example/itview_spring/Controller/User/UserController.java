package com.example.itview_spring.Controller.User;

import com.example.itview_spring.DTO.AdminUserDTO;
import com.example.itview_spring.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

        // 3. 페이지 블록 계산 로직 추가
        int currentPage = userPage.getNumber();
        int blockSize = 10; // 페이지 블록의 크기
        int startPage = (int) (Math.floor(currentPage / blockSize) * blockSize);
        int endPage = Math.min(startPage + blockSize - 1, userPage.getTotalPages() - 1);

        // 4. 페이지네이션 처리를 위한 정보도 모델에 담아 전달
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("baseUrl", "/user/list");

        // 5. 뷰의 이름 반환
        return "user/list";
    }

    // 유저 상세 조회
    @GetMapping("/user/{userid}")
    public String read(@PathVariable("userid") int id, Model model) {

        // 1. 서비스 계층을 호출하여 유저 상세 정보를 가져옵니다.
        //    ModelMapper를 사용해 AdminUserDTO로 변환된 객체를 받습니다.
        AdminUserDTO userDetail = userService.read(id);

        // 2. 조회된 DTO 객체를 모델에 담아 뷰로 전달합니다.
        model.addAttribute("user", userDetail);

        // 3. Thymeleaf 템플릿의 이름을 반환합니다.
        return "user/detail";
    }

    // 유저 수정 폼 이동
    @GetMapping("/user/{userid}/update")
    public String getUpdate(@PathVariable("userid") int id, Model model) {
        AdminUserDTO userDetail = userService.read(id);
        model.addAttribute("user", userDetail);
        return "user/update";
    }

    // 유저 수정 처리
    @PostMapping("/user/{userid}")
    public String postUpdate(@PathVariable("userid") int id, AdminUserDTO adminUserDTO) {
        userService.update(id, adminUserDTO);
        return "redirect:/user/" + id;
    }

    // 유저 삭제 처리
    @DeleteMapping("/user/{userid}")
    public String deleteUser(@PathVariable("userid") int id, RedirectAttributes redirectAttributes) {
        userService.delete(id);
        redirectAttributes.addFlashAttribute("message", "유저가 성공적으로 삭제되었습니다.");
        return "redirect:/user/list";
    }
}
