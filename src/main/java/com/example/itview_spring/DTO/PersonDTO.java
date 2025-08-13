package com.example.itview_spring.DTO;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonDTO {
    private Integer id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(length = 1024)
    private String profile;

    @Column(nullable = false, length = 255)
    private String job;
}
