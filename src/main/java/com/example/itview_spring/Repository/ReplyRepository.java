package com.example.itview_spring.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.itview_spring.Constant.Replyable;
import com.example.itview_spring.Entity.ReplyEntity;

@Repository
public interface ReplyRepository extends JpaRepository<ReplyEntity, Integer> {

    @Modifying
    @Query("DELETE FROM ReplyEntity r WHERE r.targetId = :targetId AND r.targetType = :targetType")
    void deleteByTargetIdAndTargetType(@Param("targetId") Integer targetId, @Param("targetType") Replyable targetType);
}
