package com.example.itview_spring.DTO;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContentDetailDTO {
    
    // 컨텐츠 정보
    private ContentResponseDTO contentInfo;
    // 사진 정보
    private List<ImageDTO> gallery;
    // 동영상 정보
    private List<VideoDTO> videos;
    // 외부 서비스 정보
    private List<ExternalServiceDTO> externalServices;
    // 사용자의 별점
    private Integer myRating;
    // 별점 개수
    private Long ratingCount;
    // 별점 분포
    private List<RatingCountDTO> ratingDistribution;
    // 사용자의 코멘트
    private CommentDTO myComment;
    // 컨텐츠 좋아요 상위 8개 코멘트
    private List<CommentDTO> comments;
    // 코멘트 개수
    private Long commentCount;
}