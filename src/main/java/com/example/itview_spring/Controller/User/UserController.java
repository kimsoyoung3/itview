package com.example.itview_spring.Controller.User;

import com.example.itview_spring.Config.CustomUserDetails;
import com.example.itview_spring.Constant.Role;
import com.example.itview_spring.DTO.AdminUserDTO;
import com.example.itview_spring.DTO.SuperUserDTO;
import com.example.itview_spring.Entity.UserEntity;
import com.example.itview_spring.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collection;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    // 유저 목록 조회
    @GetMapping("/user/list")
    public String list(
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
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
    public String getDetail(@PathVariable("userid") int userid, Model model, Authentication authentication) {

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        boolean isSuperAdmin = authorities.stream()
                .anyMatch(authority -> authority.getAuthority().equals(Role.SUPER_ADMIN.name()));

        AdminUserDTO userDetail = userService.read(userid);

        if (isSuperAdmin) {
            Role userRole = userService.getRoleById(userid);

            SuperUserDTO superUserDTO = new SuperUserDTO(
                    userDetail.getId(),
                    userDetail.getNickname(),
                    userDetail.getIntroduction(),
                    userDetail.getProfile(),
                    userDetail.getEmail(),
                    userRole.name()
            );
            model.addAttribute("user", superUserDTO);
        } else {
            model.addAttribute("user", userDetail);
        }

        return "user/detail";
    }

    // 유저 수정 폼 이동
    @GetMapping("/user/{userid}/update")
    public String getUpdate(@PathVariable("userid") int userid, Model model, Authentication authentication) {

        // 현재 로그인한 사용자의 ID와 권한을 확인
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        int loggedInUserId = customUserDetails.getId();
        boolean isSuperAdmin = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals(Role.SUPER_ADMIN.name()));

        // 수정하려는 유저의 정보
        AdminUserDTO userDetail = userService.read(userid);

        // 로그인한 유저가 SUPER_ADMIN이고 본인 계정이 아닌 경우
        if (isSuperAdmin && loggedInUserId != userid) {
            // SuperUserDTO를 생성하여 모델에 추가
            Role userRole = userService.getRoleById(userid);
            SuperUserDTO superUserDTO = new SuperUserDTO(
                    userDetail.getId(),
                    userDetail.getNickname(),
                    userDetail.getIntroduction(),
                    userDetail.getProfile(),
                    userDetail.getEmail(),
                    userRole.name()
            );
            model.addAttribute("user", superUserDTO);
            model.addAttribute("showRoleEdit", true); // 역할 수정 필드 표시 여부
        } else {
            // 그 외의 경우 (일반 유저, 본인 계정 수정)
            model.addAttribute("user", userDetail);
            model.addAttribute("showRoleEdit", false); // 역할 수정 필드 숨김
        }

        return "user/update";
    }

    // 유저 수정 처리
    @PostMapping("/user/{userid}")
    public String postUpdate(@PathVariable("userid") int id, SuperUserDTO superUserDTO) {
        // 폼에서 전송된 모든 데이터(닉네임, 자기소개, 역할)가 SuperUserDTO에 바인딩됩니다.
        userService.update(id, superUserDTO);
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
