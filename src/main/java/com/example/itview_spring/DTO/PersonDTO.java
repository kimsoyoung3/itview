package com.example.itview_spring.DTO;

import groovy.transform.builder.Builder;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PersonDTO {
    private Integer id;

    @NotBlank(message = "이름은 필수입니다.")
    private String name;

    @NotBlank(message = "직업은 필수입니다.")
    private String job;


    private String profile;
}



