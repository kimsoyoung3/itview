package com.example.itview_spring.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

import com.example.itview_spring.Constant.Channel;
import com.example.itview_spring.Constant.ContentType;

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
    private Integer id;                      //콘텐츠 id

    @Column(nullable = false, length = 255)
    private String title;                    //콘텐트 제목

    @Column(nullable = false, length = 255)
    @Enumerated(EnumType.STRING)
    private ContentType contentType;        //콘텐트 타입

    @Column(nullable = false)
    private LocalDate releaseDate;          //출시 날짜

    @Column(nullable = false, length = 1024)
    private String poster;                  //포스터 url

    @Column(nullable = false, length = 255)
    private String nation;                  //국가

    @Lob
    private String description;             //설명

    @Column(nullable = false, length = 255) //영화 런링타임 시리즈,회수
    private String duration;

    @Column(nullable = false, length = 255)
    private String age;                     //이용가 제한

    @Column(length = 255)
    private String creatorName;             //영화감독이름 ,책 작가이름

    @Column(length = 255)
    @Enumerated(EnumType.STRING)
    private Channel channelName;            //채널이름 (시리즈,웹툰)
}