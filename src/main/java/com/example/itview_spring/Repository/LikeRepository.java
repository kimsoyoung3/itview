package com.example.itview_spring.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.itview_spring.Entity.LikeEntity;

public interface LikeRepository extends JpaRepository<LikeEntity, Integer> {
    
    @Query("DELETE FROM LikeEntity l WHERE l.targetId = :targetId AND l.targetType = :targetType")
    void deleteByTargetIdAndTargetType(Integer targetId, String targetType);
}
