package com.example.itview_spring.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.itview_spring.DTO.VideoDTO;
import com.example.itview_spring.Entity.VideoEntity;

public interface VideoRepository extends JpaRepository<VideoEntity, Integer> {
    
    @Query("""
            SELECT new com.example.itview_spring.DTO.VideoDTO(
                v.id,
                v.title,
                v.image,
                v.url
            )
            FROM VideoEntity v
            WHERE v.content.id = :contentId
        """)
    List<VideoDTO> findByContentId(@Param("contentId") Integer contentId);
}
