package com.example.itview_spring.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "api/user/login", "api/user", "api/user/logout"
                        , "user/login", "user/register").permitAll()
                .anyRequest().permitAll()
        );

        http.csrf(AbstractHttpConfigurer::disable);

        http.formLogin(login -> login
                .loginPage("/api/user/login")
                .defaultSuccessUrl("/")
                .usernameParameter("email")
                .permitAll()
        );

        http.exceptionHandling(excptionHandling -> excptionHandling
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
        );

        http.logout(logout -> logout
                .logoutUrl("/api/user/logout")
                .logoutSuccessUrl("/")
        );

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}