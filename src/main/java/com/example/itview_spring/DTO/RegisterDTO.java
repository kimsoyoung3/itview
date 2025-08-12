package com.example.itview_spring.DTO;

import com.example.itview_spring.Constant.Role;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
