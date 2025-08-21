package com.example.itview_spring.Entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

import com.example.itview_spring.Constant.Role;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class UserEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // 유저 역할 (예: ADMIN, USER 등)
    @Column(nullable = false, length = 255)
    @Enumerated(EnumType.STRING)
    private Role role;

    // 유저 닉네임
    @Column(nullable = false, length = 255)
    private String nickname;

    // 유저 이메일
    @Column(nullable = false, length = 255)
    private String email;

    // 유저 비밀번호
    @Column(nullable = false, length = 255)
    private String password;

    // 유저 프로필 사진 링크
    @Column(length = 1024)
    private String profile;

    // 유저 소개
    @Column(length = 1024)
    private String introduction;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CollectionEntity> collections;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentEntity> comments;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RatingEntity> ratings;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WishlistEntity> wishlists;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReplyEntity> replies;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LikeEntity> likes;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EmailVerificationEntity> emailVerifications;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SocialEntity> socials;
}
