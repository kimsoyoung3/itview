package com.example.itview_spring.Entity;

import lombok.Getter;
import lombok.Setter;
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
public class VideoEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // 컨텐츠
    @JoinColumn(name = "content_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private ContentEntity content;

    // 비디오 제목
    @Column(nullable = false, length = 255)
    private String title;

    // 비디오 썸네일 URL
    @Column(nullable = false, length = 1024)
    private String image;

    // 비디오 URL
    @Column(nullable = false, length = 1024)
    private String url;
}
