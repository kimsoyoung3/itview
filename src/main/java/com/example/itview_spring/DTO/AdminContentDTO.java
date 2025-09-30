package com.example.itview_spring.DTO;

import com.example.itview_spring.Constant.Channel;
import com.example.itview_spring.Constant.ContentType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminContentDTO {
    private int id; //id
    private String title; //제목
    private ContentType contentType; //컨텐츠 타입
    private String creatorName; //(영화-감독이름, 책-작가이름, 음반-아티스트이름)
    private Channel channelName; //시리즈, 웹툰 연재처
    private String nation; //국가
    private String description; //컨텐츠 설명
    private LocalDate releaseDate; //공개일
    private String poster; //포스터사진
    private String age; //이용가 제한
    private String duration; //분량 (영화-러닝타임, 시리즈-화수, 책-쪽수, 웹툰-화수, 음반-러닝타임)
}
