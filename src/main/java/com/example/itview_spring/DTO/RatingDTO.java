package com.example.itview_spring.DTO;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RatingDTO {
    
    private ContentResponseDTO content; //컨텐츠 정보
    private int score; //해당 유저가 컨텐츠에 남긴 별점
}
