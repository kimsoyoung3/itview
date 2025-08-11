package com.example.itview_spring.Config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfing {
    
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
