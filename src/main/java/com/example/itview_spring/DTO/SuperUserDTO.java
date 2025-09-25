package com.example.itview_spring.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SuperUserDTO {
    private int id; //유저 id
    private String nickname; //닉네임
    private String introduction; //자기소개
    private String profile; //이미지 주소
    private String email;
    private String role; //역할
}
