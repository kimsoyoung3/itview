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
}
