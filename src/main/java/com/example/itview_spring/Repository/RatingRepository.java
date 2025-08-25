package com.example.itview_spring.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.itview_spring.DTO.RatingCountDTO;
import com.example.itview_spring.Entity.RatingEntity;

@Repository
public interface RatingRepository extends JpaRepository<RatingEntity, Integer> {

    // 특정 컨텐츠에 대한 사용자 평점 조회 (행 전체)
    Optional<RatingEntity> findByUserIdAndContentId(Integer userId, Integer contentId);

    // 특정 컨텐츠에 대한 사용자 평점 삭제
    void deleteByUserIdAndContentId(Integer userId, Integer contentId);

    // 특정 컨텐츠에 대한 사용자 평점 조회 (평점만)
    @Query("SELECT score FROM RatingEntity r WHERE r.content.id = :contentId AND r.user.id = :userId")
    Integer findSomeoneScore(@Param("userId") Integer userId, @Param("contentId") Integer contentId);

    // 특정 컨텐츠에 대한 평점 개수 조회
    @Query("SELECT COUNT(r) FROM RatingEntity r WHERE r.content.id = :contentId")
    Long countByContentId(@Param("contentId") Integer contentId);

    // 특정 컨텐츠에 대한 평점 분포 조회
    @Query("SELECT new com.example.itview_spring.DTO.RatingCountDTO(r.score, COUNT(r)) " +
           "FROM RatingEntity r WHERE r.content.id = :contentId " +
           "GROUP BY r.score")
    List<RatingCountDTO> findRatingDistributionByContentId(@Param("contentId") Integer contentId);
}
