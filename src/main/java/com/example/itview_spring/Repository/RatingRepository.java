package com.example.itview_spring.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.itview_spring.Entity.RatingEntity;

public interface RatingRepository extends JpaRepository<RatingEntity, Integer> {
    Optional<RatingEntity> findByUserIdAndContentId(Integer userId, Integer contentId);
    void deleteByUserIdAndContentId(Integer userId, Integer contentId);
}
