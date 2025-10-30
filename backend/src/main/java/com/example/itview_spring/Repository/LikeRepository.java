package com.example.itview_spring.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.itview_spring.Constant.Replyable;
import com.example.itview_spring.Entity.LikeEntity;

@Repository
public interface LikeRepository extends JpaRepository<LikeEntity, Integer> {
    
    @Modifying
    @Query("DELETE FROM LikeEntity l WHERE l.targetId = :targetId AND l.targetType = :targetType")
    void deleteByTargetIdAndTargetType(@Param("targetId") Integer targetId, @Param("targetType") Replyable targetType);

    // 특정 대상에 대한 사용자의 좋아요 존재 여부 확인
    Boolean existsByUserIdAndTargetIdAndTargetType(Integer userId, Integer targetId, Replyable targetType);

    // 특정 대상에 좋아요 등록
    @Modifying
    @Query("INSERT INTO LikeEntity (user.id, targetId, targetType) VALUES (:userId, :targetId, :targetType)")
    void likeTarget(@Param("userId") Integer userId, @Param("targetId") Integer targetId, @Param("targetType") Replyable targetType);

    // 특정 대상에 좋아요 취소
    @Modifying
    @Query("DELETE FROM LikeEntity l WHERE l.user.id = :userId AND l.targetId = :targetId AND l.targetType = :targetType")
    void unlikeTarget(@Param("userId") Integer userId, @Param("targetId") Integer targetId, @Param("targetType") Replyable targetType);
}
