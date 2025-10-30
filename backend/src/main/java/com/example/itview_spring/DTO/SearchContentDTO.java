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
public class SearchContentDTO {
    private List<ContentDTO> movie;
    private List<ContentDTO> series;
    private List<ContentDTO> book;
    private List<ContentDTO> webtoon;
    private List<ContentDTO> record;
}
