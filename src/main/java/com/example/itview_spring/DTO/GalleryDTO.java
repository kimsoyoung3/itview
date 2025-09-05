package com.example.itview_spring.DTO;

import com.example.itview_spring.Entity.ContentEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GalleryDTO {

    // 갤러리 ID
    private Integer id;
    // 컨텐츠 id
    private ContentEntity content;
    // 사진 URL
    private String photo;

    public void setContentId(Integer id) {
    }
}