package com.example.itview_spring.DTO;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDTO {
    private String nickname;

    private String email;

    private String password;
}
