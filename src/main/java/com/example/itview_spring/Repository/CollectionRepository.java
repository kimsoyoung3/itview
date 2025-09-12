package com.example.itview_spring.Repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.itview_spring.DTO.CollectionResponseDTO;
import com.example.itview_spring.Entity.CollectionEntity;

public interface CollectionRepository extends JpaRepository<CollectionEntity, Integer> {
    
    // 컬렉션 페이징 조회
    @Query("""
            select new com.example.itview_spring.DTO.CollectionResponseDTO(
                c.id,
                c.title,
                size(c.items),
                c.updatedAt,
                (case when exists (
                    select 1 from LikeEntity l 
                    where l.targetId = c.id and 
                        l.targetType = Replyable.COLLECTION and 
                        l.user.id = :loginUserId
                    ) then true else false end
                ),
                (select count(l2) from LikeEntity l2 
                    where l2.targetId = c.id and 
                        l2.targetType = Replyable.COLLECTION
                ),
                (select count(r) from ReplyEntity r 
                    where r.targetId = c.id and 
                        r.targetType = Replyable.COLLECTION
                ),
                c.description,
                new com.example.itview_spring.DTO.UserProfileDTO(
                    c.user.id,
                    c.user.nickname,
                    c.user.introduction,
                    c.user.profile
                )
            )
            from CollectionEntity c
            where c.user.id = :userId
            order by c.updatedAt desc
            """)
    Page<CollectionResponseDTO> findUserCollections(@Param("loginUserId") Integer loginUserId, @Param("userId") Integer userId, Pageable pageable);

    @Query("""
            select new com.example.itview_spring.DTO.CollectionResponseDTO(
                c.id,
                c.title,
                size(c.items),
                c.updatedAt,
                (case when exists (
                    select 1 from LikeEntity l 
                    where l.targetId = c.id and 
                        l.targetType = Replyable.COLLECTION and 
                        l.user.id = :loginUserId
                    ) then true else false end
                ),
                (select count(l2) from LikeEntity l2 
                    where l2.targetId = c.id and 
                        l2.targetType = Replyable.COLLECTION
                ),
                (select count(r) from ReplyEntity r 
                    where r.targetId = c.id and 
                        r.targetType = Replyable.COLLECTION
                ),
                c.description,
                new com.example.itview_spring.DTO.UserProfileDTO(
                    c.user.id,
                    c.user.nickname,
                    c.user.introduction,
                    c.user.profile
                )
            )
            from CollectionEntity c
            where c.id = :id
            """)
    CollectionResponseDTO findCollectionById(@Param("loginUserId") Integer loginUserId, @Param("id") Integer id);

    // 컬렉션 썸네일 조회
    @Query("""
            select ci.content.poster
            from CollectionItemEntity ci
            where ci.collection.id = :id
            order by ci.id desc
            limit 5
            """)
    List<String> findCollectionPosters(@Param("id") Integer id);
}
