package com.example.itview_spring.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
}