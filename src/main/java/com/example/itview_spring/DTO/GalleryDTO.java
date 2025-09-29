package com.example.itview_spring.DTO;

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
    private Integer contentId; // ✅ Entity 대신 ID만 사용 0910 수정
//    private ContentEntity content;
    // 사진 URL
    private String photo;
}