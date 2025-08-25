package com.example.itview_spring.Repository;

import com.example.itview_spring.DTO.UserProfileDTO;
import com.example.itview_spring.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    Optional<UserEntity> findByEmail(String email);

    @Query("""
            SELECT new com.example.itview_spring.DTO.UserProfileDTO(u.id, u.nickname, u.introduction, u.profile)
            FROM UserEntity u
            WHERE u.id = :id
            """)
    Optional<UserProfileDTO> findUserProfileById(@Param("id") Integer id);
}