package com.example.itview_spring.DTO;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserDTO {
    private int id; //유저 id
    private String nickname; //닉네임
    private String introduction; //자기소개
    private String profile; //이미지 주소
    private String email; //이메일
}
