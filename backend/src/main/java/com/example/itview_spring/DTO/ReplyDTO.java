package com.example.itview_spring.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReplyDTO {
    
    // 댓글 id
    private Integer id;
    // 댓글을 남긴 시각
    private LocalDateTime createdAt;
    // 사용자의 좋아요 확인
    private Boolean liked;
    // 좋아요 수
    private Long likeCount;
    // 댓글 내용
    private String text;
    // 댓글을 남긴 유저 정보
    private UserProfileDTO user;
}
