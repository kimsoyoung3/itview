package com.example.itview_spring.DTO;

import com.example.itview_spring.Constant.Channel;
import com.example.itview_spring.Constant.ContentType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContentDTO {
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
}
