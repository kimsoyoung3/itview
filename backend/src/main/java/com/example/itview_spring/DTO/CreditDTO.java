package com.example.itview_spring.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreditDTO {
    
    // 크레딧 ID
    private Integer id;
    // 인물 정보
    private PersonDTO person;
    // 배역 이름
    private String characterName;
    // 부문
    private String department;
    // 역할
    private String role;
}