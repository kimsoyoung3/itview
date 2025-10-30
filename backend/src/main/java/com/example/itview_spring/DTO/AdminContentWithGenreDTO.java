package com.example.itview_spring.DTO;

import com.example.itview_spring.Constant.Channel;
import com.example.itview_spring.Constant.ContentType;
import com.example.itview_spring.Constant.Genre;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminContentWithGenreDTO {
    private Integer id;                   // 콘텐츠 ID
    private String title;                 // 제목
    private ContentType contentType;      // 타입
    private LocalDate releaseDate;        // 공개일
    private String poster;                // 포스터 URL
    private String nation;                // 국가
    private String description;           // 설명
    private String duration;              // 길이
    private String age;                   // 연령제한
    private String creatorName;           // 제작자 이름
    private Channel channelName;          // 플랫폼

    private List<Genre> genres;           // ✔ 장르 리스트
}
