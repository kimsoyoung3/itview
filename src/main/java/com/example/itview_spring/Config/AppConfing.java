package com.example.itview_spring.Config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.itview_spring.Util.S3Uploader;

@Configuration
public class AppConfing {

    @Bean
    public S3Uploader s3Uploader() {
        return new S3Uploader();
    }
    
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}