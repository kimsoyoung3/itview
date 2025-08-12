package com.example.itview_spring.Entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

import com.example.itview_spring.Constant.Channel;
import com.example.itview_spring.Constant.ContentType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ContentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // 컨텐츠 제목
    @Column(nullable = false, length = 255)
    private String title;

    // 컨텐츠 타입
    @Column(nullable = false, length = 255)
    @Enumerated(EnumType.STRING)
    private ContentType contentType;

    // 컨텐츠 공개일
    @Column(nullable = false)
    private LocalDate releaseDate;

    // 컨텐츠 포스터 이미지 URL
    @Column(nullable = false, length = 1024)
    private String poster;

    // 컨텐츠 국가
    @Column(nullable = false, length = 255)
    private String nation;

    // 컨텐츠 설명
    @Lob
    private String description;

    // 컨텐츠 길이
    @Column(nullable = false, length = 255)
    private String duration;

    // 컨텐츠 연령 등급
    @Column(nullable = false, length = 255)
    private String age;

    // 영화-감독이름, 책-작가이름, 음반-아티스트이름
    @Column(length = 255)
    private String creatorName;

    // 플랫폼 이름
    @Column(length = 255)
    @Enumerated(EnumType.STRING)
    private Channel channelName;
}