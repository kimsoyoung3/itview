package com.example.itview_spring.Repository;

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
            SELECT new com.example.itview_spring.DTO.CommentDTO(c.id, c.createdAt, c.text)
            FROM CommentEntity c
            WHERE c.user.id = :userId AND c.content.id = :contentId
            """)
    Optional<CommentDTO> findCommentDTOByUserIdAndContentId(@Param("userId") Integer userId, @Param("contentId") Integer contentId);
}
