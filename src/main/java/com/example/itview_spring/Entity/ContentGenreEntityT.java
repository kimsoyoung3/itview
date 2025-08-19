package com.example.itview_spring.Entity;

import com.example.itview_spring.Constant.Genre;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ContentGenreEntityT {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // 컨텐츠
    @JoinColumn(name = "content_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private ContentEntity content;

    // 장르
    @Column(nullable = false, length = 255)
    @Enumerated(EnumType.STRING)
    private Genre genre;
}
