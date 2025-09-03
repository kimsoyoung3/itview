package com.example.itview_spring.Controller.User;

import com.example.itview_spring.Config.CustomUserDetails;
import com.example.itview_spring.DTO.EmailDTO;
import com.example.itview_spring.DTO.EmailVerificationDTO;
import com.example.itview_spring.DTO.LoginDTO;
import com.example.itview_spring.DTO.NewPasswordDTO;
import com.example.itview_spring.DTO.RegisterDTO;
import com.example.itview_spring.DTO.UserProfileUpdateDTO;
import com.example.itview_spring.DTO.UserResponseDTO;
import com.example.itview_spring.Service.UserService;
import com.example.itview_spring.Util.S3Uploader;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class UserRestController {
    
    private final UserService registerService;
    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;
    private final UserService userService;
    private final S3Uploader s3Uploader;

    // 회원가입
    @PostMapping
    public ResponseEntity<Void> registerPost(@RequestBody RegisterDTO registerDTO) {
        try {
            registerService.createUser(registerDTO);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build(); // 이미 가입한 회원인 경우
        }
        return ResponseEntity.ok().build();
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<Void> loginPost(@RequestBody LoginDTO loginDTO, HttpServletRequest request, HttpServletResponse response) {
        try {
            // loginDTO로 인증 토큰 생성
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    loginDTO.getEmail(), loginDTO.getPassword());

            // 인증 매니저를 사용하여 인증 시도
            Authentication authentication = authenticationManager.authenticate(authToken);

            // 인증이 성공하면 SecurityContextHolder에 인증 정보 저장
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);

            // 세션 생성
            securityContextRepository.saveContext(context, request, response);
        } catch (Exception e) {
            return ResponseEntity.status(401).build(); // 인증 실패
        }
        return ResponseEntity.ok().build();
    }

    // 현재 로그인된 사용자 정보 조회
    @GetMapping("/me")
    public ResponseEntity<Integer> me(@AuthenticationPrincipal CustomUserDetails user) {
        if (user == null) return ResponseEntity.status(401).build();
        return ResponseEntity.ok(user.getId());
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        // SecurityContext를 비워서 로그아웃 처리
        SecurityContextHolder.clearContext();
        
        // 기존 세션 무효화
        if (request.getSession(false) != null) {
            request.getSession(false).invalidate();
        }
        
        // JSESSIONID 쿠키 삭제
        Cookie cookie = new Cookie("JSESSIONID", null);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0); // 즉시 만료
        response.addCookie(cookie);
        
        return ResponseEntity.ok().build();
    }

    // 존재하는 이메일인지 확인
    @PostMapping("/email")
    public ResponseEntity<Void> emailCheck(@RequestBody EmailDTO emailDTO) {
        if (registerService.isUserExists(emailDTO.getEmail())) {
            return ResponseEntity.ok().build(); // 이미 가입된 이메일인 경우
        }
        return ResponseEntity.badRequest().build(); // 가입되지 않은 이메일인 경우
    }

    // 이메일 인증번호 생성
    @PostMapping("/createVerification")
    public ResponseEntity<Void> verificationPost(@RequestBody EmailDTO emailDTO) {
        try {
            registerService.createVerifyingCode(emailDTO);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build(); // 가입되지 않은 이메일인 경우
        }
        return ResponseEntity.ok().build();
    }

    // 이메일 인증번호 확인
    @PostMapping("/checkVerification")
    public ResponseEntity<Void> verificationGet(@RequestBody EmailVerificationDTO emailVerificationDTO) {
        try {
            boolean isValid = registerService.verifyCode(emailVerificationDTO);
            if (!isValid) {
                return ResponseEntity.badRequest().build(); // 인증 코드가 유효하지 않은 경우
            }
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build(); // 가입되지 않은 이메일인 경우
        }
        
        return ResponseEntity.ok().build();
    }

    // 비밀번호 변경
    @PostMapping("/setPW")
    public ResponseEntity<Void> setPassword(@RequestBody NewPasswordDTO newPasswordDTO) {
        try {
            registerService.setPassword(newPasswordDTO);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build(); // 비밀번호 변경 실패
        }
        return ResponseEntity.ok().build();
    }

    // 소셜 계정 연결
    @PostMapping("/link")
    public void linkGoogle(HttpServletRequest request,
                           HttpServletResponse response,
                           @AuthenticationPrincipal CustomUserDetails user,
                           @RequestParam("redirectURL") String redirectURL) throws IOException {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (user != null) { // 이미 로그인된 사용자라면 소셜 계정 연동을 위한 정보를 세션에 저장
            request.getSession().setAttribute("ORIGINAL_AUTH", auth);
            request.getSession().setAttribute("LINK_FLOW", Boolean.TRUE);
            request.getSession().setAttribute("USER_ID", user.getId());
        }
        Cookie redirectCookie = new Cookie("REDIRECT_URL", redirectURL);
        redirectCookie.setPath("/");
        redirectCookie.setHttpOnly(false);
        response.addCookie(redirectCookie);
        System.out.println("redirectURL: " + redirectURL);
    }

    // 유저 페이지 정보 조회
    @GetMapping("/{id}")
    public UserResponseDTO getUserProfile(@PathVariable("id") Integer id) {
        try {
            return registerService.getUserProfile(id);
        } catch (Exception e) {
            throw new IllegalStateException("유저 정보를 불러오는데 실패했습니다.");
        }
    }

    // 유저 프로필 수정
    @PutMapping
    public UserResponseDTO updateUserProfile(@AuthenticationPrincipal CustomUserDetails user,
                                  @ModelAttribute UserProfileUpdateDTO userProfileUpdateDTO) {
        try {
            if (user.getId() != userProfileUpdateDTO.getId()) {
                throw new IllegalStateException("본인의 프로필만 수정할 수 있습니다.");
            } else {
                userService.updateUserProfile(userProfileUpdateDTO);
                return userService.getUserProfile(userProfileUpdateDTO.getId());
            }
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage());
        }
    }
}