package com.example.itview_spring.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.itview_spring.Constant.ContentType;
import com.example.itview_spring.DTO.RatingCountDTO;
import com.example.itview_spring.DTO.RatingDTO;
import com.example.itview_spring.DTO.UserContentCountDTO;
import com.example.itview_spring.DTO.UserRatingCountDTO;
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

    // 특정 사용자의 평점 개수 조회 (컨텐츠 타입별)
    @Query("""
        SELECT new com.example.itview_spring.DTO.UserRatingCountDTO(
            COUNT(CASE WHEN c.contentType = 'MOVIE' THEN 1 END),
            COUNT(CASE WHEN c.contentType = 'SERIES' THEN 1 END),
            COUNT(CASE WHEN c.contentType = 'BOOK' THEN 1 END),
            COUNT(CASE WHEN c.contentType = 'WEBTOON' THEN 1 END),
            COUNT(CASE WHEN c.contentType = 'RECORD' THEN 1 END)
        )
        FROM ContentEntity c JOIN RatingEntity r ON c.id = r.content.id
        WHERE r.user.id = :userId
    """)
    UserRatingCountDTO findUserRatingCount(@Param("userId") Integer userId);

    // 특정 사용자의 특정 컨텐츠 타입의 별점과 위시리스트 개수 조회
    @Query("""
            SELECT new com.example.itview_spring.DTO.UserContentCountDTO(
                COUNT(r),
                (SELECT COUNT(w) FROM WishlistEntity w WHERE w.user.id = :userId AND w.content.contentType = :contentType)
            )
            FROM RatingEntity r
            WHERE r.user.id = :userId AND r.content.contentType = :contentType
            """)
    UserContentCountDTO findUserContentCount(@Param("userId") Integer userId, @Param("contentType") ContentType contentType);

    // 특정 사용자의 특정 컨텐츠 타입의 평점 목록 조회
    @Query("""
            SELECT new com.example.itview_spring.DTO.RatingDTO(
                new com.example.itview_spring.DTO.ContentResponseDTO(
                    c.id,
                    c.title,
                    c.contentType,
                    c.creatorName,
                    c.nation,
                    c.description,
                    c.releaseDate,
                    c.poster,
                    c.age,
                    c.duration,
                    (SELECT AVG(r2.score) FROM RatingEntity r2 WHERE r2.content.id = c.id)
                ),
                r.score
            )
            FROM RatingEntity r
            JOIN ContentEntity c ON r.content.id = c.id
            WHERE r.user.id = :userId AND r.content.contentType = :contentType
            ORDER BY 
                CASE WHEN :order = 'my_score_high' THEN r.score END DESC,
                CASE WHEN :order = 'my_score_low' THEN r.score END ASC,
                CASE WHEN :order = 'avg_score_high' THEN (SELECT AVG(r2.score) FROM RatingEntity r2 WHERE r2.content.id = c.id) END DESC,
                CASE WHEN :order = 'avg_score_low' THEN (SELECT AVG(r2.score) FROM RatingEntity r2 WHERE r2.content.id = c.id) END ASC,
                CASE WHEN :order = 'new' THEN r.createdAt END DESC,
                CASE WHEN :order = 'old' THEN r.createdAt END ASC
        """)
    Page<RatingDTO> findUserContentRatings(Pageable pageable, @Param("userId") Integer userId, @Param("contentType") ContentType contentType, @Param("order") String order);
}