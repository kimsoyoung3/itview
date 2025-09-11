package com.example.itview_spring.Repository;

import com.example.itview_spring.DTO.UserProfileDTO;
import com.example.itview_spring.DTO.UserResponseDTO;
import com.example.itview_spring.Entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    Optional<UserEntity> findByEmail(String email);

    @Query("""
            SELECT new com.example.itview_spring.DTO.UserProfileDTO(u.id, u.nickname, u.introduction, u.profile)
            FROM UserEntity u
            WHERE u.id = :id
            """)
    UserProfileDTO findUserProfileById(@Param("id") Integer id);

    @Query("""
        select new com.example.itview_spring.DTO.UserResponseDTO(
            new com.example.itview_spring.DTO.UserProfileDTO(u.id, u.nickname, u.introduction, u.profile),
            (select count(r.id) from RatingEntity r where r.user.id = u.id),
            (select count(com.id) from CommentEntity com where com.user.id = u.id),
            (select count(col.id) from CollectionEntity col where col.user.id = u.id),
            (select count(l1.id) from LikeEntity l1 where l1.user.id = u.id and l1.targetType = Replyable.PERSON),
            (select count(l2.id) from LikeEntity l2 where l2.user.id = u.id and l2.targetType = Replyable.COLLECTION),
            (select count(l3.id) from LikeEntity l3 where l3.user.id = u.id and l3.targetType = Replyable.COMMENT)
        )
        from UserEntity u
        where u.id = :id
            """)
    UserResponseDTO findUserResponseById(@Param("id") Integer id);

    @Query("SELECT u FROM UserEntity u WHERE u.nickname LIKE %:keyword% OR u.email LIKE %:keyword%")
    Page<UserEntity> findByKeyword(@Param("keyword") String keyword, Pageable pageable);
}