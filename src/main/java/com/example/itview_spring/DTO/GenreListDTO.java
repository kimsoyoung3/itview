package com.example.itview_spring.DTO;

import com.example.itview_spring.Constant.Genre;
import com.example.itview_spring.Entity.ContentEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GenreListDTO {
    private Integer id;            // 장르 ID (수정 시 필요)
    private ContentEntity content; //컨텐츠
    private Genre genre;           // 장르 ENUM
}
