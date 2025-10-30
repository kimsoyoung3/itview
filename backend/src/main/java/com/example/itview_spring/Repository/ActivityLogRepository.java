package com.example.itview_spring.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.itview_spring.Entity.ActivityLogEntity;
import com.example.itview_spring.Constant.ActivityLogType;



public interface ActivityLogRepository extends JpaRepository<ActivityLogEntity, Integer> {
    ActivityLogEntity findByReferenceIdAndType(Integer referenceId, ActivityLogType type);
    void deleteByReferenceIdAndType(Integer referenceId, ActivityLogType type);

    @Query("""
        SELECT new com.example.itview_spring.Entity.ActivityLogEntity(
            a.id,
            a.user,
            a.type,
            a.referenceId,
            a.timestamp,
            a.isUpdate
        )
        FROM ActivityLogEntity a
        WHERE a.user.id IN (
            SELECT f.following.id
            FROM FollowEntity f
            WHERE f.follower.id = :userId
        )
        ORDER BY a.timestamp DESC
    """)
    Page<ActivityLogEntity> findFriendActivities(@Param("userId") Integer userId, Pageable pageable);
}
