package com.example.itview_spring.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.itview_spring.Constant.ContentType;
import com.example.itview_spring.DTO.ContentResponseDTO;
import com.example.itview_spring.Entity.WishlistEntity;

@Repository
public interface WishlistRepository extends JpaRepository<WishlistEntity, Integer> {
    
    // 위시리스트 삭제
    @Modifying
    @Query("""
            DELETE FROM WishlistEntity w WHERE w.user.id = :userId AND w.content.id = :contentId
            """)
    void deleteByUserIdAndContentId(@Param("userId") Integer userId, @Param("contentId") Integer contentId);

    // 위시리스트 존재 여부 확인
    @Query("""
            SELECT CASE WHEN COUNT(w) > 0 THEN TRUE ELSE FALSE END
            FROM WishlistEntity w
            WHERE w.user.id = :userId AND w.content.id = :contentId
            """)
    Boolean existsByUserIdAndContentId(@Param("userId") Integer userId, @Param("contentId") Integer contentId);

    // 특정 사용자의 특정 컨텐츠 타입의 위시리스트 목록 조회
    @Query("""
            SELECT new com.example.itview_spring.DTO.ContentResponseDTO(
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
                (SELECT AVG(r.score) FROM RatingEntity r WHERE r.content.id = c.id)
            )
            FROM WishlistEntity w
            JOIN w.content c
            WHERE w.user.id = :userId AND c.contentType = :contentType
            ORDER BY 
                CASE WHEN :order = 'new' THEN w.createdAt END DESC,
                CASE WHEN :order = 'old' THEN w.createdAt END ASC,
                CASE WHEN :order = 'rating_high' THEN (SELECT AVG(r.score) FROM RatingEntity r WHERE r.content.id = c.id) END DESC,
                CASE WHEN :order = 'rating_low' THEN (SELECT AVG(r.score) FROM RatingEntity r WHERE r.content.id = c.id) END ASC
            """)
    Page<ContentResponseDTO> findWishlistByUserIdAndContentType(@Param("userId") Integer userId, @Param("contentType") ContentType contentType, Pageable pageable, @Param("order") String order);
}
