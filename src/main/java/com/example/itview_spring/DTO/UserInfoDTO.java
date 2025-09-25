package com.example.itview_spring.DTO;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDTO {
    
    private Integer userId;
    private Boolean google;
    private Boolean kakao;
    private Boolean naver;
}
