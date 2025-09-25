package com.example.itview_spring.Repository;

import java.util.List;
import java.util.Optional;

import com.example.itview_spring.Entity.ContentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.itview_spring.DTO.CollectionFormDTO;
import com.example.itview_spring.DTO.CollectionResponseDTO;
import com.example.itview_spring.DTO.CollectionToAddDTO;
import com.example.itview_spring.DTO.ContentResponseDTO;
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

    // 컬렉션 상세 조회
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

    // 컬렉션 폼 조회 (수정폼)
    @Query("""
            select new com.example.itview_spring.DTO.CollectionFormDTO(
                c.title,
                c.description
            )
            from CollectionEntity c
            where c.id = :id
            """)
    CollectionFormDTO findCollectionFormById(@Param("id") Integer id);

    // 컬렉션 아이템 ID 전체 조회
    @Query("""
            select ci.content.id
            from CollectionItemEntity ci
            where ci.collection.id = :id
            order by ci.id desc
            """)
    List<Integer> findCollectionItemIdsById(@Param("id") Integer id);

    // 컬렉션 아이템 페이징 조회
    @Query("""
        SELECT new com.example.itview_spring.DTO.ContentResponseDTO(
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
            (SELECT AVG(r.score) FROM RatingEntity r WHERE r.content.id = c.content.id)
        )
        FROM CollectionItemEntity c
        WHERE c.collection.id = :id
        order by c.id desc
    """)
    Page<ContentResponseDTO> findCollectionItemsById(@Param("id") Integer id, Pageable pageable);

    // 컬렉션 썸네일 조회
    @Query("""
            select ci.content.poster
            from CollectionItemEntity ci
            where ci.collection.id = :id
            order by ci.id desc
            limit 5
            """)
    List<String> findCollectionPosters(@Param("id") Integer id);

    // 특정 유저가 좋아요한 컬렉션 조회
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
            from LikeEntity l
            join CollectionEntity c on l.targetId = c.id
            where l.user.id = :userId and l.targetType = 'COLLECTION'
           """)
    Page<CollectionResponseDTO> findCollectionUserLike(@Param("loginUserId") Integer loginUserId, @Param("userId") Integer userId, Pageable pageable);

    // 컬렉션 검색
        @Query("""
            select new com.example.itview_spring.DTO.CollectionResponseDTO(
                c.id,
                c.title,
                size(c.items),
                c.updatedAt,
                false,
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
            where c.title LIKE %:keyword%
            order by c.updatedAt desc
            """)
    Page<CollectionResponseDTO> searchCollections(@Param("keyword") String keyword, Pageable pageable);

    // 컬렉션에 추가 조회
    @Query("""
        select new com.example.itview_spring.DTO.CollectionToAddDTO(
                (case when exists (
                    select 1 from CollectionItemEntity ci 
                    where ci.collection.id = c.id and 
                        ci.content.id = :contentId
                    ) then true else false end
                ),
                new com.example.itview_spring.DTO.CollectionResponseDTO(
                    c.id,
                    c.title,
                    size(c.items),
                    c.updatedAt,
                    (case when exists (
                        select 1 from LikeEntity l 
                        where l.targetId = c.id and 
                            l.targetType = Replyable.COLLECTION and 
                            l.user.id = :userId
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
            )
        from CollectionEntity c
        where c.user.id = :userId
        """)
    Page<CollectionToAddDTO> findCollectionsToAdd(@Param("userId") Integer userId, @Param("contentId") Integer contentId, Pageable pageable);

    // 특정 유저의 모든 컬렉션 ID 조회
    @Query("SELECT c.id FROM CollectionEntity c WHERE c.user.id = :userId")
    List<Integer> findAllIdsByUserId(@Param("userId") Integer userId);

    @Query("SELECT c FROM CollectionEntity c JOIN c.user u WHERE c.title LIKE %:keyword% OR u.nickname LIKE %:keyword%")
    Page<CollectionEntity> findByTitleOrUserNicknameContaining(@Param("keyword") String keyword, Pageable pageable);

    Optional<CollectionEntity> findById(Integer id);

    @Query("SELECT c.content FROM CollectionEntity co JOIN co.items c WHERE co.id = :collectionId")
    Page<ContentEntity> findContentsByCollectionId(@Param("collectionId") Integer collectionId, Pageable pageable);
}
