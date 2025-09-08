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
@AllArgsConstructor
@NoArgsConstructor
public class ContentDTO {
    private Integer id;

    // 컨텐츠 제목
    private String title;

    // 컨텐츠 타입
    private ContentType contentType;

    // 컨텐츠 공개일

    private LocalDate releaseDate;

    // 컨텐츠 포스터 이미지 URL
    private String poster;

    // 컨텐츠 국가
    private String nation;

    // 컨텐츠 설명
    private String description;

    // 컨텐츠 길이
    private String duration;

    // 컨텐츠 연령 등급
    private String age;

    // 영화-감독이름, 책-작가이름, 음반-아티스트이름
    private String creatorName;

    // 플랫폼 이름
    private Channel channelName;
}