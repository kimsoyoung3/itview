package com.example.itview_spring.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.itview_spring.Entity.EmailVerificationEntity;

public interface EmailVerificationRepository extends JpaRepository<EmailVerificationEntity, Integer> {
    @Query(value = """
                SELECT e.code
                FROM email_verification_entity e
                WHERE user_id = :userId
                AND expired_time >= NOW()
                ORDER BY expired_time DESC
                LIMIT 1
                """, nativeQuery = true)
    String findCode(@Param("userId") Integer userId);
}
