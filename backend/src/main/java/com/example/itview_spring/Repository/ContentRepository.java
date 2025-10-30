package com.example.itview_spring.Repository;

import com.example.itview_spring.Constant.Channel;
import com.example.itview_spring.Constant.ContentType;
import com.example.itview_spring.Constant.Genre;
import com.example.itview_spring.DTO.ContentDTO;
import com.example.itview_spring.DTO.ContentResponseDTO;
import com.example.itview_spring.Entity.ContentEntity;

import java.util.List;

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
            (SELECT AVG(r.score) FROM RatingEntity r WHERE r.content.id = c.id)
        )
        FROM ContentEntity c
        WHERE c.id = :id
    """)
    ContentResponseDTO findContentWithAvgRating(@Param("id") Integer id);

    // 컨텐츠 ID로 컨텐츠 제목 조회
    @Query("SELECT c.title FROM ContentEntity c WHERE c.id = :id")
    String findTitleById(@Param("id") Integer id);

    // 컨텐츠 제목으로 검색
    Page<ContentEntity> findByTitleContainingOrderByReleaseDateDesc(String title, Pageable pageable);

    // 컨텐츠 분야별 검색
    @Query("""
        SELECT new com.example.itview_spring.DTO.ContentDTO(
            c.id,
            c.title,
            c.contentType,
            c.releaseDate,
            c.poster,
            c.nation,
            c.description,
            c.duration,
            c.age,
            c.creatorName,
            c.channelName
        )
        FROM ContentEntity c
        WHERE c.title LIKE %:keyword% AND c.contentType = :contentType
        ORDER BY c.releaseDate DESC
            """)
    Page<ContentDTO> searchContents(@Param("keyword") String keyword, @Param("contentType") ContentType contentType, Pageable pageable);

    // 컨텐츠 타입 별 목록 조회 (메인 화면)
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
        FROM ContentEntity c
        WHERE c.contentType = :contentType
        ORDER BY c.releaseDate DESC
        LIMIT 10
            """)
    List<ContentResponseDTO> findByContentType(@Param("contentType") ContentType contentType);

    // 컨텐츠 타입 별 목록 조회 (페이징)
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
        FROM ContentEntity c
        WHERE c.contentType = :contentType
        ORDER BY c.releaseDate DESC
            """)
    Page<ContentResponseDTO> findByContentType(@Param("contentType") ContentType contentType, Pageable pageable);

    // 컨텐츠 타입 별 장르 목록 조회
    @Query("SELECT DISTINCT cg.genre FROM ContentGenreEntity cg WHERE cg.content.contentType = :contentType")
    List<Genre> findGenresByContentType(@Param("contentType") ContentType contentType);

    // 컨텐츠 타입 별 채널 목록 조회
    @Query("SELECT DISTINCT c.channelName FROM ContentEntity c WHERE c.contentType = :contentType AND c.channelName IS NOT NULL")
    List<Channel> findChannelsByContentType(@Param("contentType") ContentType contentType);

    // 장르 별 컨텐츠 조회
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
        FROM ContentEntity c
        JOIN ContentGenreEntity cg ON c.id = cg.content.id
        WHERE c.contentType = :contentType AND cg.genre = :genre
        ORDER BY c.releaseDate DESC
            """)
    Page<ContentResponseDTO> findByContentTypeAndGenre(@Param("contentType") ContentType contentType, @Param("genre") Genre genre, Pageable pageable);

    // 채널 별 컨텐츠 조회
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
        FROM ContentEntity c
        WHERE c.contentType = :contentType AND c.channelName = :channel
        ORDER BY c.releaseDate DESC
            """)
    Page<ContentResponseDTO> findByContentTypeAndChannel(@Param("contentType") ContentType contentType, @Param("channel") Channel channel, Pageable pageable);

    @Query("SELECT c FROM ContentEntity c " +
            "WHERE (:keyword IS NULL OR c.title LIKE %:keyword%) " +
            "AND (:contentType IS NULL OR c.contentType = :contentType)")
    Page<ContentEntity> searchByKeywordAndType(
            @Param("keyword") String keyword,
            @Param("contentType") ContentType contentType,
            Pageable pageable
    );
}