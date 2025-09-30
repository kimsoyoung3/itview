package com.example.itview_spring.Config;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.example.itview_spring.Constant.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

import com.example.itview_spring.Entity.SocialEntity;
import com.example.itview_spring.Entity.UserEntity;
import com.example.itview_spring.Repository.SocialRepository;
import com.example.itview_spring.Repository.UserRepository;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserRepository userRepository;
    private final SocialRepository socialRepository;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/assets/**", "/admin/register", "/admin/login").permitAll()
                        .requestMatchers("/api/**").permitAll()
                        .requestMatchers("/", "/collection/**", "/comment/**", "/content/**", "/person/**", "/reply/**", "/user/**").hasAnyAuthority(Role.ADMIN.name(), Role.SUPER_ADMIN.name())
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/admin/login")
                        .loginProcessingUrl("/admin/login")
                        .failureUrl("/admin/login?error")
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/admin/logout")
                        .logoutSuccessUrl("/admin/login")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.sendRedirect(request.getContextPath() + "/admin/login");
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.sendRedirect(request.getContextPath() + "/admin/login?access-denied");
                        })
                )
                .oauth2Login(oauth2 -> oauth2
                        .successHandler(linkingSuccessHandler())
                )
                .headers((headers) -> headers.frameOptions().sameOrigin());

        return http.build();
    }

    @Bean
    AuthenticationSuccessHandler linkingSuccessHandler() {
        return (request, response, authentication) -> {
            var session = request.getSession(false);
            var isLinkFlow = session != null && Boolean.TRUE.equals(session.getAttribute("LINK_FLOW"));
            String redirectURL = "http://localhost:3000/"; // 기본 리다이렉트 URL
            // String redirectURL = "https://itview.kro.kr/#"; // 기본 리다이렉트 URL
            for (Cookie cookie : request.getCookies()) {
                if ("REDIRECT_URL".equals(cookie.getName())) {
                    redirectURL = cookie.getValue(); // 세션에 저장된 리다이렉트 URL 사용
                    System.out.println("Redirect URL from cookie: " + redirectURL);
                    break;
                }
            }
            System.out.println("Redirecting to: " + redirectURL);
            // "연동"인 경우에만 이 블록 수행
            if (isLinkFlow && authentication instanceof OAuth2AuthenticationToken oauth) {
                var principal = oauth.getPrincipal();

                // provider 확인
                String provider = oauth.getAuthorizedClientRegistrationId();

                String sub = "";
                if (provider.equals("google")) {
                    sub = principal.getAttribute("sub").toString();
                } else if (provider.equals("kakao")) {
                    // kakao는 id가 고유 식별자
                    sub = principal.getAttribute("id").toString();
                } else if (provider.equals("naver")) {
                    // naver는 response 안에 id가 고유 식별자
                    Map<String, Object> responseMap = principal.getAttribute("response");
                    sub = responseMap.get("id").toString();
                } else {
                    throw new IllegalStateException("지원하지 않는 소셜 로그인 제공자입니다.");
                }

                // 이미 등록된 소셜 계정인지 확인
                Optional<SocialEntity> existing = socialRepository.findByProviderAndProviderId(provider, sub);
                if (existing.isEmpty()) { // 등록되지 않은 경우
                    // 사용자 확인
                    Integer userId = (Integer) session.getAttribute("USER_ID");
                    UserEntity user = userRepository.findById(userId)
                            .orElseThrow(() -> new IllegalStateException("사용자를 찾을 수 없습니다."));

                    // 소셜 계정 정보 저장
                    SocialEntity socialEntity = new SocialEntity();
                    socialEntity.setProvider(provider);
                    socialEntity.setProviderId(sub);
                    socialEntity.setUser(user);
                    socialRepository.save(socialEntity);
                }

                // 기존 로그인 복구용 인증 꺼내기
                var original = (Authentication) session.getAttribute("ORIGINAL_AUTH");

                // 보안컨텍스트를 기존 인증으로 되돌리기(= 로그인 유지)
                if (original != null) {
                    SecurityContextHolder.getContext().setAuthentication(original);
                }

                // 세션 정리
                session.removeAttribute("ORIGINAL_AUTH");
                session.removeAttribute("LINK_FLOW");
                session.removeAttribute("USER_ID");

                // 플래시 메시지 설정 및 리다이렉트
                sendFlashMessageAndRedirect(response,
                        existing.isPresent() ? "이미 등록된 소셜 계정입니다." : "소셜 계정이 성공적으로 연동되었습니다.",
                        redirectURL
                );

                return;
            }

            // 연동이 아닌 일반 로그인 처리
            if (authentication instanceof OAuth2AuthenticationToken oauth) {
                try {
                    // OAuth2 인증 정보에서 사용자 정보 가져오기
                    var principal = oauth.getPrincipal();
                    String provider = oauth.getAuthorizedClientRegistrationId();
                    String sub = "";
                    if (provider.equals("google")) {
                        sub = principal.getAttribute("sub").toString();
                    } else if (provider.equals("kakao")) {
                        // kakao는 id가 고유 식별자
                        sub = principal.getAttribute("id").toString();
                    } else if (provider.equals("naver")) {
                        // naver는 response 안에 id가 고유 식별자
                        Map<String, Object> responseMap = principal.getAttribute("response");
                        sub = responseMap.get("id").toString();
                    } else {
                        throw new IllegalStateException("지원하지 않는 소셜 로그인 제공자입니다.");
                    }

                    // 소셜 계정이 등록되어 있는지 확인
                    Optional<SocialEntity> linked = socialRepository.findByProviderAndProviderId(provider, sub);

                    // 소셜 계정이 등록되어 있는 경우
                    if (linked.isPresent()) {
                        // 사용자 정보 가져오기
                        UserEntity user = linked.get().getUser();

                        // 사용자 권한 설정
                        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                        authorities.add(new SimpleGrantedAuthority(user.getRole().name()));

                        // CustomUserDetails 생성
                        var customUserDetails = new CustomUserDetails(
                                user.getId(),
                                user.getNickname(),
                                user.getPassword(),
                                authorities
                        );

                        // 새로운 인증 객체 생성
                        Authentication userAuth = new UsernamePasswordAuthenticationToken(customUserDetails, null, authorities);

                        // 보안 컨텍스트에 인증 객체 설정
                        request.getSession(true);
                        SecurityContextHolder.getContext().setAuthentication(userAuth);

                        // 플래시 메시지 설정 및 리다이렉트
                        sendFlashMessageAndRedirect(response, "소셜 로그인에 성공했습니다.", redirectURL);

                        return;
                    } else {
                        // 소셜 계정이 등록되지 않은 경우
                        SecurityContextHolder.clearContext();
                        HttpSession httpSession = request.getSession(false);
                        if (httpSession != null) {
                            httpSession.invalidate();
                        }
                        sendFlashMessageAndRedirect(response, "등록되지 않은 소셜 계정입니다.", redirectURL);
                        return;
                    }
                } catch (Exception e) {
                    // 예외 발생 시 플래시 메시지 설정
                    e.printStackTrace();
                    SecurityContextHolder.clearContext();
                    HttpSession httpSession = request.getSession(false);
                    if (httpSession != null) {
                        httpSession.invalidate();
                    }
                    sendFlashMessageAndRedirect(response, "소셜 로그인 처리 중 오류가 발생했습니다.", redirectURL);
                    return;
                }
            }
        };
    }

    // 리다이렉트와 함께 플래시 메시지를 설정하는 메소드
    private void sendFlashMessageAndRedirect(HttpServletResponse response, String message, String redirectURL) throws IOException {
        String encoded = URLEncoder.encode(message, StandardCharsets.UTF_8).replace("+", "%20");
        Cookie flash = new Cookie("FLASH_ERROR", encoded);
        flash.setHttpOnly(false);
        flash.setPath("/");
        flash.setMaxAge(10); // 10초 유지
        response.addCookie(flash);
        response.sendRedirect(redirectURL);
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