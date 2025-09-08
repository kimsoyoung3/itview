package com.example.itview_spring.Repository;

import com.example.itview_spring.Entity.ContentEntity;
import com.example.itview_spring.Entity.ContentGenreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.itview_spring.DTO.GenreDTO;
import java.util.List;

@Repository
public interface ContentGenreRepository extends JpaRepository<ContentGenreEntity, Integer> {

    /**
     * 특정 콘텐츠의 장르 엔티티 전체 조회
     */
    List<ContentGenreEntity> findByContent (ContentEntity content);

    /**
     * 특정 콘텐츠 ID로 모든 장르 삭제
     */
    void deleteByContentId(Integer contentId);

    /**
     * 특정 콘텐츠에 특정 장르가 이미 존재하는지 확인
     */
    boolean existsByContentIdAndGenre(Integer contentId, com.example.itview_spring.Constant.Genre genre);

    // 특정 컨텐츠 ID로 장르 DTO 리스트 조회
    @Query(value = """
                SELECT new com.example.itview_spring.DTO.GenreDTO(cg.genre)
                FROM ContentGenreEntity cg
                where cg.content.id = :contentId
                """)
    List<GenreDTO> findByContentId(@Param("contentId") Integer contentId);
}