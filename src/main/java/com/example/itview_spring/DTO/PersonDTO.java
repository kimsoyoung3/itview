package com.example.itview_spring.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@NotBlank
@NotNull
public class PersonDTO {
    private Integer id;

    private String name;

    private String job;

    private MultipartFile profileImage;


}

