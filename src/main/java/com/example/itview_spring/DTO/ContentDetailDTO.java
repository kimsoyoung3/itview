package com.example.itview_spring.DTO;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContentDetailDTO {
    
    // 컨텐츠 정보
    ContentResponseDTO contentInfo;
    // 사진 정보
    List<ImageDTO> gallery;
    // 동영상 정보
    List<VideoDTO> videos;
    // 외부 서비스 정보
    List<ExternalServiceDTO> externalServices;
}