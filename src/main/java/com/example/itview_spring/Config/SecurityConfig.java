package com.example.itview_spring.Config;

import java.lang.foreign.Linker.Option;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

import com.example.itview_spring.Entity.SocialEntity;
import com.example.itview_spring.Entity.UserEntity;
import com.example.itview_spring.Repository.SocialRepository;
import com.example.itview_spring.Repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserRepository userRepository;
    private final SocialRepository socialRepository;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/api/user/login", "/api/user", "/api/user/logout"
                        , "/user/login", "/user/register").permitAll()
                .anyRequest().permitAll()
        );

        http.headers((headers) -> headers.frameOptions().sameOrigin());

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
                String sub = principal.getAttribute("sub");

                // provider 확인
                String provider = oauth.getAuthorizedClientRegistrationId();

                // 사용자 ID 확인
                Integer userId = (Integer) session.getAttribute("USER_ID");
                UserEntity user = userRepository.findById(userId)
                        .orElseThrow(() -> new IllegalStateException("사용자를 찾을 수 없습니다."));

                // 연동 로직 처리
                SocialEntity socialEntity = new SocialEntity();
                socialEntity.setProvider(provider);
                socialEntity.setProviderId(sub);
                socialEntity.setUser(user);
                socialRepository.save(socialEntity);

                // 기존 로그인 복구용 인증 꺼내기
                var original = (Authentication) session.getAttribute("ORIGINAL_AUTH");

                // 보안컨텍스트를 기존 인증으로 되돌리기(= 로그인 유지)
                if (original != null) {
                    SecurityContextHolder.getContext().setAuthentication(original);
                }

                // 세션 정리 및 리다이렉트
                session.removeAttribute("ORIGINAL_AUTH");
                session.removeAttribute("LINK_FLOW");
                session.removeAttribute("USER_ID");
                response.sendRedirect("http://localhost:3000/"); // “연동 완료” 페이지
                return;
            }

            if (authentication instanceof OAuth2AuthenticationToken oauth) {
                try {
                    var principal = oauth.getPrincipal();
                    String provider = oauth.getAuthorizedClientRegistrationId();
                    String sub = principal.getAttribute("sub");
                    System.out.println("Provider: " + provider);
                    System.out.println("Sub: " + sub);
    
                    Optional<SocialEntity> linked = socialRepository.findByProviderAndProviderId(provider, sub);
                    System.out.println("Linked SocialEntity: " + linked);
    
                    if (linked.isPresent()) {
                        UserEntity user = linked.get().getUser();
                        System.out.println("Linked UserEntity: " + user);
    
                        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                        authorities.add(new SimpleGrantedAuthority(user.getRole().name()));
    
                        var customUserDetails = new CustomUserDetails(
                            user.getId(),
                            user.getNickname(),
                            user.getPassword(),
                            authorities
                        );
    
                        Authentication userAuth = new UsernamePasswordAuthenticationToken(customUserDetails, null, authorities);
    
                        request.getSession(true);
                        SecurityContextHolder.getContext().setAuthentication(userAuth);
                        response.sendRedirect("http://localhost:3000/");
                        return;
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    response.sendRedirect("http://localhost:3000/error");
                    return;
                }
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