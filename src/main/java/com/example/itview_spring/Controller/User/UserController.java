package com.example.itview_spring.Controller.User;

import com.example.itview_spring.DTO.LoginDTO;
import com.example.itview_spring.DTO.RegisterDTO;
import com.example.itview_spring.Service.RegisterService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    
    private final RegisterService registerService;
    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;

    @PostMapping
    public ResponseEntity<Void> registerPost(@RequestBody RegisterDTO registerDTO) {
        registerService.createUser(registerDTO);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<Void> loginPost(@RequestBody LoginDTO loginDTO, HttpServletRequest request, HttpServletResponse response) {
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

        return ResponseEntity.ok().build();
    }
}
