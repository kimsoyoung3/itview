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
    
    private String contentType;
    private String department;

    public WorkDomainDTO(ContentType contentType, String department) {
        this.contentType = contentType.getDescription();
        this.department = department;
    }
}
