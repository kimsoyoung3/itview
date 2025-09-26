package com.example.itview_spring.Entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.example.itview_spring.Constant.NotiType;
import com.example.itview_spring.Constant.Replyable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
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
@EntityListeners(AuditingEntityListener.class)
public class NotificationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // 알림을 받을 유저
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    // 알림을 발생시킨 유저
    @ManyToOne
    @JoinColumn(name = "actor_id", nullable = false)
    private UserEntity actor;

    // 알림 타입
    @Column(nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private NotiType type;

    // 타겟 타입
    @Column(nullable = false)
    private Replyable targetType;

    // 타겟 ID
    @Column(nullable = false)
    private Integer targetId;

    // 발생시각
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
