package com.example.itview_spring.DTO;

import com.example.itview_spring.Constant.Channel;
import com.example.itview_spring.Constant.ContentType;
import com.example.itview_spring.Constant.Genre;
import com.example.itview_spring.Entity.ContentEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateListDTO {
    private Integer id;            // 장르 ID (수정 시 필요)
    private ContentEntity content; //컨텐츠
    private Genre genre;           // 장르 ENUM
}
