package com.example.itview_spring.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.itview_spring.Entity.ActivityLogEntity;
import com.example.itview_spring.Constant.ActivityLogType;


public interface ActivityLogRepository extends JpaRepository<ActivityLogEntity, Integer> {
    void deleteByReferenceIdAndType(Integer referenceId, ActivityLogType type);
}
