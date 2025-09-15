package com.example.itview_spring.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminContentDTO {
    // 콘텐츠 id
    private Integer id;

    // 콘텐츠 제목
    private String title;

    // 포스터 이미지 URL
    private String poster;
}
