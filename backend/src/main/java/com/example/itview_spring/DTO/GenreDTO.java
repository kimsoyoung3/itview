package com.example.itview_spring.DTO;

import com.example.itview_spring.Constant.Genre;

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
public class GenreDTO {

    Genre genre;
}