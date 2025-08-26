package com.example.itview_spring.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.itview_spring.Entity.ReplyEntity;

public interface ReplyRepository extends JpaRepository<ReplyEntity, Integer> {
    
    @Query("DELETE FROM ReplyEntity r WHERE r.targetId = :targetId AND r.targetType = :targetType")
    void deleteByTargetIdAndTargetType(Integer targetId, String targetType);
}
