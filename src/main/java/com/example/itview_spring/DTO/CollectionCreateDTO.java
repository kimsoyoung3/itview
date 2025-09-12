package com.example.itview_spring.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CollectionCreateDTO {
    private String title; // 컬렉션 제목
    private String description; // 컬렉션 설명
    private List<ContentDTO> contents; // 컬렉션에 포함된 컨텐츠 목록
}
