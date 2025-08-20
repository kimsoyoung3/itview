package com.example.itview_spring.DTO;

import java.time.LocalDate;
import java.util.List;

import com.example.itview_spring.Constant.ContentType;

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
public class ContentResponseDTO {
    private Integer id;
    private String title;
    private ContentType contentType;
    private String creatorName;
    private String nation;
    private String description;
    private LocalDate releaseDate;
    private String poster;
    private List<GenreDTO> genres;
    private String age;
    private String duration;
    private Double ratingAvg;

    public ContentResponseDTO(Integer id, String title, ContentType contentType, String creatorName, String nation,
                              String description, LocalDate releaseDate, String poster, String age, String duration,
                              Double ratingAvg) {
        this.id = id;
        this.title = title;
        this.contentType = contentType;
        this.creatorName = creatorName;
        this.nation = nation;
        this.description = description;
        this.releaseDate = releaseDate;
        this.poster = poster;
        this.age = age;
        this.duration = duration;
        this.ratingAvg = ratingAvg;
    }
}