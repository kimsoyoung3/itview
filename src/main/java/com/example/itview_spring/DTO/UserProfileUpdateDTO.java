package com.example.itview_spring.DTO;

import lombok.Getter;
import lombok.Setter;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileUpdateDTO {
    
    private int id; //유저 id
    private String nickname; //이름
    private String introduction; //자기소개
    private MultipartFile profile; //프로필 사진
}
