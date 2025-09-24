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
public class HomeContentDTO {
    private List<ContentResponseDTO> movie;
    private List<ContentResponseDTO> series;
    private List<ContentResponseDTO> book;
    private List<ContentResponseDTO> webtoon;
    private List<ContentResponseDTO> record;
}
