package com.example.itview_spring.DTO;

import lombok.Getter;
import lombok.Setter;

import com.example.itview_spring.Constant.ContentType;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WorkDomainDTO {
    
    private ContentType contentType;
    private String department;
}
