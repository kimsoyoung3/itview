package com.example.itview_spring.DTO;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonResponseDTO {
    private int id;
    private String name;
    private String profile;
    private String job;
    private Boolean liked;
    private Long likeCount;
}
