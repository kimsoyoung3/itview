package com.example.itview_spring.DTO;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDTO {
    
    // 유저 id
    private Integer id;
    // 유저 닉네임
    private String nickname;
    // 자기소개
    private String introduction;
    // 프로필 사진 URL
    private String profile;
}
