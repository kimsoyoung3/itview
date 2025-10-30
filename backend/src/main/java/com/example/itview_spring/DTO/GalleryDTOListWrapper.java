package com.example.itview_spring.DTO;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor

/**
 * 갤러리 전체 교체 시 List<GalleryDTO> 를 안전하게 바인딩하기 위한 Wrapper
 */
@Data
public class GalleryDTOListWrapper {
    private List<GalleryDTO> galleryDTOs = new ArrayList<>();
}