package com.example.itview_spring.Entity;

import lombok.Getter;
import lombok.Setter;

import com.example.itview_spring.Constant.Genre;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class ContentGenreEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // 컨텐츠
    @JoinColumn(name = "content_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private ContentEntity content;

    // 장르
    @Column(nullable = false, length = 255)
    @Enumerated(EnumType.STRING)
    private Genre genre;


}
