package com.example.itview_spring.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.itview_spring.Entity.NotificationEntity;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Integer> {
    
}
