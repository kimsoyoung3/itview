package com.example.itview_spring.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.itview_spring.Entity.EmailVerificationEntity;

public interface EmailVerificationRepository extends JpaRepository<EmailVerificationEntity, Integer> {
    
}
