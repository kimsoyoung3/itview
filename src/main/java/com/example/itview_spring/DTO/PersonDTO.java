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

    // 이름
    @Column(nullable = false, length = 255)
    private String name;

    // 프로필 사진 URL
    @Column(length = 1024)
    private String profile;

    // 직업
    @Column(nullable = false, length = 255)
    private String job;

    private MultipartFile profileImage;


}

