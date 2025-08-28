package com.example.itview_spring.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ImageDTO {
    
    // 이미지 ID
    private Integer id;

    // 이미지 URL
    private String imageUrl;
}