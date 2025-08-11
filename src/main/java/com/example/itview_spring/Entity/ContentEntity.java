package com.example.itview_spring.Entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

import com.example.itview_spring.Constant.Channel;
import com.example.itview_spring.Constant.ContentType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ContentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false, length = 255)
    @Enumerated(EnumType.STRING)
    private ContentType contentType;

    @Column(nullable = false)
    private LocalDate releaseDate;

    @Column(nullable = false, length = 1024)
    private String poster;

    @Column(nullable = false, length = 255)
    private String nation;

    @Lob
    private String description;

    @Column(nullable = false, length = 255)
    private String duration;

    @Column(nullable = false, length = 255)
    private String age;

    @Column(length = 255)
    private String creatorName;

    @Column(length = 255)
    @Enumerated(EnumType.STRING)
    private Channel channelName;
}