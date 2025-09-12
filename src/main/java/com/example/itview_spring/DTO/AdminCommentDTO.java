package com.example.itview_spring.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminCommentDTO {

    // 코멘트 id
    private Integer id;
    // 코멘트를 남긴 시각
    private LocalDateTime createdAt;
    // 코멘트 내용
    private String text;
}
