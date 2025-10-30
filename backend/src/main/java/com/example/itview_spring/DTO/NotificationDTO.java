package com.example.itview_spring.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDTO {
    private String profile;
    private Integer actorId;
    private String title;
    private String link;
    private LocalDateTime createdAt;
}
