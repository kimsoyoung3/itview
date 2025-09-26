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
import com.example.itview_spring.DTO.CommentAndContentDTO;
import com.example.itview_spring.DTO.CommentDTO;
import com.example.itview_spring.Entity.CommentEntity;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Integer> {
    Optional<CommentEntity> findByUserIdAndContentId(Integer userId, Integer contentId);

    // CommentDTO
    @Query("""
            SELECT new com.example.itview_spring.DTO.CommentDTO(
                c.id,
                c.createdAt,
                case when (exists (
                    select 1 from LikeEntity l2
                    where l2.targetId = c.id and l2.targetType = 'COMMENT' and l2.user.id = :userId
                )) then true else false end,
                (select count(l) from LikeEntity l where l.targetId = c.id and l.targetType = 'COMMENT'),
                (select count(r) from ReplyEntity r where r.targetId = c.id and r.targetType = 'COMMENT'),
                c.text,
                new com.example.itview_spring.DTO.UserProfileDTO(
                    c.user.id,
                    c.user.nickname,
                    c.user.introduction,
                    c.user.profile
                ),
                (select r.score from RatingEntity r where r.user.id = :userId and r.content.id = :contentId)
            )
            FROM CommentEntity c
            WHERE c.user.id = :userId AND c.content.id = :contentId
            """)
    Optional<CommentDTO> findCommentDTOByUserIdAndContentId(@Param("userId") Integer userId, @Param("contentId") Integer contentId);

    // 특정 컨텐츠의 좋아요 상위 8개 코멘트 조회
    @Query("""
            SELECT new com.example.itview_spring.DTO.CommentDTO(
                c.id,
                c.createdAt,
                case when (exists (
                    select 1 from LikeEntity l2
                    where l2.targetId = c.id and l2.targetType = 'COMMENT' and l2.user.id = :userId
                )) then true else false end,
                (select count(l) from LikeEntity l where l.targetId = c.id and l.targetType = 'COMMENT'),
                (select count(r) from ReplyEntity r where r.targetId = c.id and r.targetType = 'COMMENT'),
                c.text,
                new com.example.itview_spring.DTO.UserProfileDTO(
                    c.user.id,
                    c.user.nickname,
                    c.user.introduction,
                    c.user.profile
                ),
                (select r.score from RatingEntity r where r.user.id = c.user.id and r.content.id = :contentId)
            )
            FROM CommentEntity c
            WHERE c.content.id = :contentId
            ORDER BY (select count(l) from LikeEntity l where l.targetId = c.id and l.targetType = 'COMMENT') DESC
            LIMIT 8
            """)
    List<CommentDTO> findTop8CommentsByContentId(@Param("userId") Integer userId, @Param("contentId") Integer contentId);

    // 특정 컨텐츠의 코멘트 페이징 조회
    @Query("""
            SELECT new com.example.itview_spring.DTO.CommentDTO(
                c.id,
                c.createdAt,
                case when (exists (
                    select 1 from LikeEntity l2
                    where l2.targetId = c.id and l2.targetType = 'COMMENT' and l2.user.id = :userId
                )) then true else false end,
                (select count(l) from LikeEntity l where l.targetId = c.id and l.targetType = 'COMMENT'),
                (select count(r) from ReplyEntity r where r.targetId = c.id and r.targetType = 'COMMENT'),
                c.text,
                new com.example.itview_spring.DTO.UserProfileDTO(
                    c.user.id,
                    c.user.nickname,
                    c.user.introduction,
                    c.user.profile
                ),
                (select r.score from RatingEntity r where r.user.id = c.user.id and r.content.id = :contentId)
            )
            FROM CommentEntity c
            WHERE c.content.id = :contentId
            ORDER BY 
                CASE WHEN :order = 'new' THEN c.createdAt END DESC,
                CASE WHEN :order = 'old' THEN c.createdAt END ASC,
                CASE WHEN :order = 'like' THEN (select count(l) from LikeEntity l where l.targetId = c.id and l.targetType = 'COMMENT') END DESC,
                CASE WHEN :order = 'reply' THEN (select count(r) from ReplyEntity r where r.targetId = c.id and r.targetType = 'COMMENT') END DESC,
                CASE WHEN :order = 'rating' THEN (select r.score from RatingEntity r where r.user.id = c.user.id and r.content.id = :contentId) END DESC
            """)
    Page<CommentDTO> findByContentId(@Param("userId") Integer userId, @Param("contentId") Integer contentId, @Param("order") String order, Pageable pageable);

    // 코멘트 개수 조회
    @Query("SELECT COUNT(c) FROM CommentEntity c WHERE c.content.id = :contentId")
    Long countByContentId(@Param("contentId") Integer contentId);

    // 코멘트 + 컨텐츠 정보
    @Query("""
            SELECT new com.example.itview_spring.DTO.CommentAndContentDTO(
                new com.example.itview_spring.DTO.CommentDTO(
                    c.id,
                    c.createdAt,
                    case when (exists (
                        select 1 from LikeEntity l2
                        where l2.targetId = c.id and l2.targetType = 'COMMENT' and l2.user.id = :userId
                    )) then true else false end,
                    (select count(l) from LikeEntity l where l.targetId = c.id and l.targetType = 'COMMENT'),
                    (select count(r) from ReplyEntity r where r.targetId = c.id and r.targetType = 'COMMENT'),
                    c.text,
                    new com.example.itview_spring.DTO.UserProfileDTO(
                        c.user.id,
                        c.user.nickname,
                        c.user.introduction,
                        c.user.profile
                    ),
                    (select r.score from RatingEntity r where r.user.id = c.user.id and r.content.id = c.content.id)
                ),
                new com.example.itview_spring.DTO.ContentResponseDTO(
                    c.content.id,
                    c.content.title,
                    c.content.contentType,
                    c.content.creatorName,
                    c.content.nation,
                    c.content.description,
                    c.content.releaseDate,
                    c.content.poster,
                    c.content.age,
                    c.content.duration,
                    (select AVG(r2.score) from RatingEntity r2 where r2.content.id = c.content.id)
                )
            )
            FROM CommentEntity c
            WHERE c.id = :commentId
            """)
    Optional<CommentAndContentDTO> findCommentAndContentByCommentId(@Param("commentId") Integer commentId, @Param("userId") Integer userId);

    // 유저의 코멘트 + 컨텐츠 목록 조회 (페이징)
    @Query("""
            SELECT new com.example.itview_spring.DTO.CommentAndContentDTO(
                new com.example.itview_spring.DTO.CommentDTO(
                    c.id,
                    c.createdAt,
                    case when (exists (
                        select 1 from LikeEntity l2
                        where l2.targetId = c.id and l2.targetType = 'COMMENT' and l2.user.id = :loginUserId
                    )) then true else false end,
                    (select count(l) from LikeEntity l where l.targetId = c.id and l.targetType = 'COMMENT'),
                    (select count(r) from ReplyEntity r where r.targetId = c.id and r.targetType = 'COMMENT'),
                    c.text,
                    new com.example.itview_spring.DTO.UserProfileDTO(
                        c.user.id,
                        c.user.nickname,
                        c.user.introduction,
                        c.user.profile
                    ),
                    (select r.score from RatingEntity r where r.user.id = c.user.id and r.content.id = c.content.id)
                ),
                new com.example.itview_spring.DTO.ContentResponseDTO(
                    c.content.id,
                    c.content.title,
                    c.content.contentType,
                    c.content.creatorName,
                    c.content.nation,
                    c.content.description,
                    c.content.releaseDate,
                    c.content.poster,
                    c.content.age,
                    c.content.duration,
                    (select AVG(r2.score) from RatingEntity r2 where r2.content.id = c.content.id)
                )
            )
            FROM CommentEntity c
            WHERE c.user.id = :userId and c.content.contentType = :contentType
            ORDER BY 
                CASE WHEN :order = 'recent' THEN c.createdAt END DESC,
                CASE WHEN :order = 'like' THEN (select count(l) from LikeEntity l where l.targetId = c.id and l.targetType = 'COMMENT') END DESC,
                CASE WHEN :order = 'reply' THEN (select count(r) from ReplyEntity r where r.targetId = c.id and r.targetType = 'COMMENT') END DESC,
                CASE WHEN :order = 'rating' THEN (select r.score from RatingEntity r where r.user.id = c.user.id and r.content.id = c.content.id) END DESC,
                CASE WHEN :order = 'new' THEN c.content.releaseDate END DESC
            """)
    Page<CommentAndContentDTO> findCommentAndContentByUserId(@Param("loginUserId") Integer loginUserId, @Param("userId") Integer userId, @Param("contentType") ContentType contentType, Pageable pageable, @Param("order") String order);

    // 유저가 좋아요한 코멘트 조회
    @Query("""
            select new com.example.itview_spring.DTO.CommentAndContentDTO(
                new com.example.itview_spring.DTO.CommentDTO(
                    c.id,
                    c.createdAt,
                    case when (exists (
                        select 1 from LikeEntity l2
                        where l2.targetId = c.id and l2.targetType = 'COMMENT' and l2.user.id = :loginUserId
                    )) then true else false end,
                    (select count(l) from LikeEntity l where l.targetId = c.id and l.targetType = 'COMMENT'),
                    (select count(r) from ReplyEntity r where r.targetId = c.id and r.targetType = 'COMMENT'),
                    c.text,
                    new com.example.itview_spring.DTO.UserProfileDTO(
                        c.user.id,
                        c.user.nickname,
                        c.user.introduction,
                        c.user.profile
                    ),
                    (select r.score from RatingEntity r where r.user.id = c.user.id and r.content.id = c.content.id)
                ),
                new com.example.itview_spring.DTO.ContentResponseDTO(
                    c.content.id,
                    c.content.title,
                    c.content.contentType,
                    c.content.creatorName,
                    c.content.nation,
                    c.content.description,
                    c.content.releaseDate,
                    c.content.poster,
                    c.content.age,
                    c.content.duration,
                    (select AVG(r2.score) from RatingEntity r2 where r2.content.id = c.content.id)
                )
            )
            from LikeEntity l
            join CommentEntity c on l.targetId = c.id and l.targetType = 'COMMENT'
            where l.user.id = :userId and l.targetType = 'COMMENT'
            order by l.id desc
            """)
    Page<CommentAndContentDTO> findCommentAndContentUserLike(@Param("loginUserId") Integer loginUserId, @Param("userId") Integer userId, Pageable pageable);

    // 특정 유저의 모든 코멘트 ID 조회
    @Query("SELECT c.id FROM CommentEntity c WHERE c.user.id = :userId")
    List<Integer> findAllIdsByUserId(@Param("userId") Integer userId);

    // 특정 코멘트의 모든 댓글 유저 ID 조회
    @Query("SELECT DISTINCT r.user.id FROM ReplyEntity r WHERE r.targetId = :commentId AND r.targetType = 'COMMENT'")
    List<Integer> findAllReplyUserIdsByCommentId(@Param("commentId") Integer commentId);

    Page<CommentEntity> findByUserId(Integer userId, Pageable pageable);
}