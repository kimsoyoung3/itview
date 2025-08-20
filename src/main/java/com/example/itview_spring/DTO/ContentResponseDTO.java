package com.example.itview_spring.DTO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.example.itview_spring.Constant.ContentType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ContentResponseDTO {

    // 컨텐츠 id
    private Integer id;
    // 컨텐츠 제목
    private String title;
    // 컨텐츠 타입
    private ContentType contentType;
    // 제작자 이름
    private String creatorName;
    // 국가
    private String nation;
    // 설명
    private String description;
    // 출시일
    private LocalDate releaseDate;
    // 포스터 이미지 URL
    private String poster;
    // 장르 리스트
    private List<String> genres = new ArrayList<>();
    // 연령 등급
    private String age;
    // 길이
    private String duration;
    // 평균 별점
    private Double ratingAvg;

    public ContentResponseDTO(Integer id, String title, ContentType contentType, String creatorName, String nation,
                              String description, LocalDate releaseDate, String poster, String age, String duration,
                              Double ratingAvg) {
        this.id = id;
        this.title = title;
        this.contentType = contentType;
        this.creatorName = creatorName;
        this.nation = nation;
        this.description = description;
        this.releaseDate = releaseDate;
        this.poster = poster;
        this.age = age;
        this.duration = duration;
        this.ratingAvg = ratingAvg;
    }
}