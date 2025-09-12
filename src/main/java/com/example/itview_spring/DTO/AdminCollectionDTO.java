package com.example.itview_spring.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminCollectionDTO {
    private int id; //컬렉션 id
    private String title; //컬렉션 이름
    private String description; //컬렉션 설명
    private LocalDateTime updatedAt;
    private UserProfileDTO user;
}
