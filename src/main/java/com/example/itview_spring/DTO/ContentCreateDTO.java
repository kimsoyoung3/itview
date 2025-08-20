package com.example.itview_spring.DTO;

import com.example.itview_spring.Constant.Channel;
import com.example.itview_spring.Constant.ContentType;
import com.example.itview_spring.Constant.Genre;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContentCreateDTO {
    private Integer id;
    private String title;
    private ContentType contentType;
    private LocalDate releaseDate;
    private String poster;
    private String nation;
    private String description;
    private String duration;
    private String age;
    private String creatorName;
    private Channel channelName;
    // 선택 사항: genre 필드 추가
    private List<Genre> genres;
}
