package com.example.itview_spring.Repository;

import java.util.List;
import java.util.Optional;

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

    // Entity 조회 (목록)
    List<VideoEntity> findByContent_Id(Integer contentId);

    //0825 추가함
    // ✔️ 콘텐츠 ID로 단일 영상 조회 (Entity 반환)
    // Entity 조회 (단일)
    Optional<VideoEntity> findFirstByContent_Id(Integer contentId);

    // 비디오를 ID로 조회
    Optional<VideoEntity> findById(Integer videoId);

}
