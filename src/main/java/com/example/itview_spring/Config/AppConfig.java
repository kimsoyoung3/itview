package com.example.itview_spring.Config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode;

import com.example.itview_spring.Util.S3Uploader;

@Configuration
@EnableSpringDataWebSupport(pageSerializationMode = PageSerializationMode.VIA_DTO)
public class AppConfig {

    @Bean
    public S3Uploader s3Uploader() {
        return new S3Uploader();
    }
    
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}