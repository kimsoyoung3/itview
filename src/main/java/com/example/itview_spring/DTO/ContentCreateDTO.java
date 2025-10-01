package com.example.itview_spring.DTO;

import com.example.itview_spring.Constant.Channel;
import com.example.itview_spring.Constant.ContentType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

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
}
