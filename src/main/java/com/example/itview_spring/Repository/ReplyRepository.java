package com.example.itview_spring.Repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.itview_spring.Constant.Replyable;
import com.example.itview_spring.DTO.ReplyDTO;
import com.example.itview_spring.Entity.ReplyEntity;

@Repository
public interface ReplyRepository extends JpaRepository<ReplyEntity, Integer> {

    @Modifying
    @Query("DELETE FROM ReplyEntity r WHERE r.targetId = :targetId AND r.targetType = :targetType")
    void deleteByTargetIdAndTargetType(@Param("targetId") Integer targetId, @Param("targetType") Replyable targetType);
    
    // 댓글 Id로 조회
    @Query("""
            SELECT new com.example.itview_spring.DTO.ReplyDTO(
                r.id,
                r.createdAt,
                case when (exists (
                    select 1 from LikeEntity l2
                    where l2.targetId = r.id and l2.targetType = 'REPLY' and l2.user.id = :userId
                )) then true else false end,
                (select count(l) from LikeEntity l where l.targetId = r.id and l.targetType = 'REPLY'),
                r.text,
                new com.example.itview_spring.DTO.UserProfileDTO(
                    r.user.id,
                    r.user.nickname,
                    r.user.introduction,
                    r.user.profile
                )
            )
            FROM ReplyEntity r
            WHERE r.id = :replyId
            """)
    ReplyDTO findReplyDTOById(@Param("userId") Integer userId, @Param("replyId") Integer replyId);

    // 특정 대상의 댓글 페이징 조회
    @Query("""
            SELECT new com.example.itview_spring.DTO.ReplyDTO(
                r.id,
                r.createdAt,
                case when (exists (
                    select 1 from LikeEntity l2
                    where l2.targetId = r.id and l2.targetType = 'REPLY' and l2.user.id = :userId
                )) then true else false end,
                (select count(l) from LikeEntity l where l.targetId = r.id and l.targetType = 'REPLY'),
                r.text,
                new com.example.itview_spring.DTO.UserProfileDTO(
                    r.user.id,
                    r.user.nickname,
                    r.user.introduction,
                    r.user.profile
                )
            )
            FROM ReplyEntity r
            WHERE r.targetId = :targetId AND r.targetType = :targetType
            ORDER BY r.createdAt DESC
            """)
    Page<ReplyDTO> findRepliesByTargetId(@Param("userId") Integer userId, @Param("targetId") Integer targetId, @Param("targetType") Replyable targetType, Pageable pageable);

    Page<ReplyEntity> findByUserIdAndTargetType(int userId, Replyable targetType, Pageable pageable);
    // 특정 컬렉션의 모든 댓글 id 조회
    @Query("SELECT r.id FROM ReplyEntity r WHERE r.targetId = :collectionId AND r.targetType = 'COLLECTION'")
    List<Integer> findAllByCollectionId(@Param("collectionId") Integer collectionId);

    Page<ReplyEntity> findByUserId(int userId, Pageable pageable);
}
