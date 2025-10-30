package com.example.itview_spring.Entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class PersonEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // 이름
    @Column(nullable = false, length = 255)
    private String name;

    // 프로필 사진 링크
    @Column(length = 1024)
    private String profile;

    // 직업
    @Column(nullable = false, length = 255)
    private String job;

    // 인물의 출연 작품 목록
    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CreditEntity> credits;
}
