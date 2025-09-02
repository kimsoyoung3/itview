package com.example.itview_spring.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

import com.example.itview_spring.Constant.ContentType;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WorkDTO {
    private int id;
    private String poster;
    private String contentType;
    private String title;
    private LocalDate releaseDate;
    private String department;
    private String role;
    private Double ratingAvg;
    private List<ExternalServiceDTO> externalServices;

    public WorkDTO(int id, String poster, ContentType contentType, String title, LocalDate releaseDate, String department, String role, Double ratingAvg) {
        this.id = id;
        this.poster = poster;
        this.contentType = contentType.getDescription();
        this.title = title;
        this.releaseDate = releaseDate;
        this.department = department;
        this.role = role;
        this.ratingAvg = ratingAvg;
    }
}
