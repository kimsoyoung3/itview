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
public class CommentDTO {
    
    // 코멘트 id
    private Integer id;
    // 코멘트를 남긴 시각
    private LocalDateTime createdAt;
    // 사용자의 좋아요 확인
    private Boolean liked;
    // 좋아요 수
    private Long likeCount;
    // 댓글 수
    private Long replyCount;
    // 코멘트 내용
    private String text;
    // 코멘트를 남긴 유저 정보
    private UserProfileDTO user;
    // 코멘트를 작성한 유저의 별점 정보
    private Integer rating;
}
