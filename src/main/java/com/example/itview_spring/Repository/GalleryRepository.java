package com.example.itview_spring.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.itview_spring.DTO.ImageDTO;
import com.example.itview_spring.Entity.GalleryEntity;

@Repository
public interface GalleryRepository extends JpaRepository<GalleryEntity, Integer> {

    @Query(value = """
                SELECT new com.example.itview_spring.DTO.ImageDTO(g.id, g.photo)
                FROM GalleryEntity g
                WHERE g.content.id = :contentId
                """)
    List<ImageDTO> findByContentId(Integer contentId);
}
