package com.example.itview_spring.Entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

import com.example.itview_spring.Constant.ActivityLogType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class ActivityLogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ActivityLogType type;

    @Column(nullable = false)
    private Integer referenceId; // 예: CommentEntity_id, CollectionEntity_id 등

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false)
    private Boolean isUpdate = false; // 수정 여부
}