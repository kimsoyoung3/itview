package com.example.itview_spring.Repository;

import com.example.itview_spring.DTO.ContentResponseDTO;
import com.example.itview_spring.Entity.ContentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ContentRepository extends JpaRepository<ContentEntity, Integer> {
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
}
