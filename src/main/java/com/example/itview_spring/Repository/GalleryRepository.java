package com.example.itview_spring.Repository;

import java.util.List;

import com.example.itview_spring.DTO.GalleryDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.itview_spring.DTO.ImageDTO;
import com.example.itview_spring.Entity.GalleryEntity;

@Repository
public interface GalleryRepository extends JpaRepository<GalleryEntity, Integer> {

    // 컨텐츠 ID로 이미지 리스트 조회
    @Query(value = """
                SELECT new com.example.itview_spring.DTO.ImageDTO(g.id, g.photo)
                FROM GalleryEntity g
                WHERE g.content.id = :contentId
                """)
    List<ImageDTO> findByContentId(@Param("contentId") Integer contentId);

    List<GalleryDTO> findGallerysByContentId(Integer contentId);

    void deleteByContentId(Integer contentId);

    boolean existsByContentIdAndPhoto(Integer contentId, String photo);
}