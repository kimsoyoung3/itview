package com.example.itview_spring.DTO;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRatingCountDTO {

    private Long movie; //영화 별점개수
    private Long series; //시리즈 별점개수
    private Long book; //책 별점개수
    private Long webtoon; //웹툰 별점개수
    private Long record; //음반 별점개수
}
