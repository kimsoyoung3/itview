package com.example.itview_spring.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.itview_spring.DTO.RatingCountDTO;
import com.example.itview_spring.Entity.RatingEntity;

public interface RatingRepository extends JpaRepository<RatingEntity, Integer> {

    Optional<RatingEntity> findByUserIdAndContentId(Integer userId, Integer contentId);

    void deleteByUserIdAndContentId(Integer userId, Integer contentId);

    @Query("SELECT score FROM RatingEntity r WHERE r.content.id = :contentId AND r.user.id = :userId")
    Integer findSomeoneScore(@Param("userId") Integer userId, @Param("contentId") Integer contentId);

    @Query("SELECT COUNT(r) FROM RatingEntity r WHERE r.content.id = :contentId")
    Long countByContentId(@Param("contentId") Integer contentId);

    @Query("SELECT new com.example.itview_spring.DTO.RatingCountDTO(r.score, COUNT(r)) " +
           "FROM RatingEntity r WHERE r.content.id = :contentId " +
           "GROUP BY r.score")
    List<RatingCountDTO> findRatingDistributionByContentId(@Param("contentId") Integer contentId);
}
