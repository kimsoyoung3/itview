package com.example.itview_spring.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VideoDTO {

    // 비디오 ID
    private Integer id;
    // 영상 제목
    private String title;
    // 썸네일 URL
    private String image;
    // 영상 URL
    private String url;
}