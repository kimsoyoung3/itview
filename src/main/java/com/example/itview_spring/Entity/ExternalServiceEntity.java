package com.example.itview_spring.Entity;

import lombok.Getter;
import lombok.Setter;

import com.example.itview_spring.Constant.Channel;

import jakarta.persistence.CascadeType;
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
public class ExternalServiceEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // 컨텐츠
    @JoinColumn(name = "content_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private ContentEntity content;

    // 외부 서비스 타입 (예: 넷플릭스, 왓챠 등)
    @Column(nullable = false, length = 255)
    @Enumerated(EnumType.STRING)
    private Channel type;

    // 외부 서비스 링크
    @Column(nullable = false, length = 1024)
    private String href;
}