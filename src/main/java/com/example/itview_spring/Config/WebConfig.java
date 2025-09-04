package com.example.itview_spring.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {
    @Bean
    public org.springframework.web.filter.HiddenHttpMethodFilter hiddenHttpMethodFilter(){
        return new org.springframework.web.filter.HiddenHttpMethodFilter();
    }
}
