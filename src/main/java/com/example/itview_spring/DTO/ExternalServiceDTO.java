package com.example.itview_spring.DTO;

import com.example.itview_spring.Constant.Channel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExternalServiceDTO {
    
    // 외부 서비스 ID
    private Integer id;
    // 외부 서비스 타입
    private Channel type;
    // 외부 서비스 URL
    private String href;
}
