package com.example.itview_spring.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/api/user/login", "/api/user", "/api/user/logout"
                        , "/user/login", "/user/register").permitAll()
                .anyRequest().permitAll()
        );

        http
            .csrf(csrf -> csrf.disable())
            .formLogin(form -> form.disable())
            .httpBasic(basic -> basic.disable())
            .oauth2Login(oauth2 -> oauth2
                .successHandler(linkingSuccessHandler())
            );

        return http.build();
    }

    @Bean
    AuthenticationSuccessHandler linkingSuccessHandler() {
        var defaultHandler = new SavedRequestAwareAuthenticationSuccessHandler();
        return (request, response, authentication) -> {
            var session = request.getSession(false);
            var isLinkFlow = (session != null) && Boolean.TRUE.equals(session.getAttribute("LINK_FLOW"));
            if (isLinkFlow && authentication instanceof OAuth2AuthenticationToken oauth) {
                // "연동"인 경우에만 이 블록 수행
                var principal = oauth.getPrincipal();
                String sub; // 구글 고유 식별자
                if (principal instanceof org.springframework.security.oauth2.core.oidc.user.OidcUser oidc) {
                    sub = oidc.getSubject(); // OIDC 표준
                } else {
                    sub = String.valueOf(principal.getAttributes().get("sub")); // OAuth2User fallback
                }

                System.out.println("Social account: " + sub);

                // 1) 기존(자체) 로그인 복구용 인증 꺼내기
                var original = (Authentication) session.getAttribute("ORIGINAL_AUTH");

                // 2) DB에 연동 저장 (provider=GOOGLE, sub, email 등)
                //    예: userSocialService.linkGoogleSub(original.getName(), sub, email);
                //    * sub+provider는 UNIQUE 제약 권장
                //    * 이미 다른 계정에 연동된 sub면 오류 처리

                // 3) 보안컨텍스트를 기존 인증으로 되돌리기(= 로그인 유지)
                if (original != null) {
                    SecurityContextHolder.getContext().setAuthentication(original);
                }

                // 4) 세션 정리 및 리다이렉트
                session.removeAttribute("ORIGINAL_AUTH");
                session.removeAttribute("LINK_FLOW");
                response.sendRedirect("http://localhost:3000/"); // “연동 완료” 페이지
                return;
            }

            // 일반적인 구글 로그인(연동 아님)은 원래대로 진행
            defaultHandler.onAuthenticationSuccess(request, response, authentication);
        };
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }
}