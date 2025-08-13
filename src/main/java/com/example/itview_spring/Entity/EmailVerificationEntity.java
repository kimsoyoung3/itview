package com.example.itview_spring.Entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

import org.springframework.cglib.core.Local;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class EmailVerificationEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // 유저
    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private UserEntity user;

    // 인증 코드
    @Column(nullable = false, length = 255)
    private String code;

    // 유효 시간
    @Column(nullable = false)
    private LocalDateTime expired_time = LocalDateTime.now().plusMinutes(10);
}
