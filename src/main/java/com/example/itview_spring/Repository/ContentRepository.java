package com.example.itview_spring.Repository;

import com.example.itview_spring.DTO.ContentDTO;
import com.example.itview_spring.DTO.ContentResponseDTO;
import com.example.itview_spring.Entity.ContentEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentRepository extends JpaRepository<ContentEntity, Integer> {

    // 컨텐츠 ID로 컨텐츠 정보 조회
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
            AVG(r.score)
        )
        FROM ContentEntity c
        LEFT JOIN RatingEntity r ON r.content.id = c.id
        WHERE c.id = :id
        GROUP BY c.id, c.title, c.contentType, c.creatorName, c.nation,
                c.description, c.releaseDate, c.poster, c.age, c.duration
    """)
    ContentResponseDTO findContentWithAvgRating(@Param("id") Integer id);

    // 컨텐츠 ID로 컨텐츠 제목 조회
    @Query("SELECT c.title FROM ContentEntity c WHERE c.id = :id")
    String findTitleById(@Param("id") Integer id);

    // 컨텐츠 제목으로 검색
    Page<ContentEntity> findByTitleContainingOrderByReleaseDateDesc(String title, Pageable pageable);
}