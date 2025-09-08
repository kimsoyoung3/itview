package com.example.itview_spring.DTO;

import com.example.itview_spring.Constant.Channel;
import com.example.itview_spring.Constant.ContentType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContentCreateDTO {
    private Integer id;
    private String title;
    private ContentType contentType;
    private LocalDate releaseDate;
    private String poster;
    private String nation;
    private String description;
    private String duration;
    private String age;
    private String creatorName;
    private Channel channelName;
    // 선택 사항: genre 필드 추가
    //이 필드는 등록/수정 시 유용합니다. create 및 update 시
    // 이 필드를 활용해서 addGenres, updateGenres 호출이 가능
    // 장르 리스트 (GenreDTO 또는 GenreCreateDTO 사용 추천)
    private List<GenreDTO> genres;

    // 비디오 리스트
    private List<VideoDTO> videos;

    // 외부서비스 리스트
    private List<ExternalServiceDTO> externalServices;
}
