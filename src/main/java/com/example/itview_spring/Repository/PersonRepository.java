package com.example.itview_spring.Repository;

import com.example.itview_spring.DTO.PersonDTO;
import com.example.itview_spring.DTO.PersonResponseDTO;
import com.example.itview_spring.Entity.PersonEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface PersonRepository extends JpaRepository <PersonEntity, Integer> {

    @Query("""
            SELECT new com.example.itview_spring.DTO.PersonResponseDTO(
                p.id,
                p.name,
                p.profile,
                p.job,
                CASE WHEN (EXISTS (
                    SELECT 1 FROM LikeEntity l1
                    WHERE l1.targetId = p.id AND l1.targetType = 'PERSON' AND l1.user.id = :userId
                )) THEN true ELSE false END,
                (SELECT COUNT(l2) FROM LikeEntity l2 WHERE l2.targetId = p.id AND l2.targetType = 'PERSON')
            )
            FROM PersonEntity p
            WHERE p.id = :personId
            """)
    PersonResponseDTO findPersonResponseDTO(@Param("userId") Integer userId, @Param("personId") Integer personId);

    @Query("""
            select new com.example.itview_spring.DTO.PersonDTO(
                p.id,
                p.name,
                p.profile,
                p.job
            )
            from LikeEntity l
            join PersonEntity p on l.targetId = p.id and l.targetType = 'PERSON'
            where l.user.id = :id and l.targetType = 'PERSON'
            order by l.id desc
            """)
    Page<PersonDTO> findPersonUserLike(@Param("id") Integer id, Pageable pageable);

    PersonEntity findByName(String name);
    // 🔍 이름 부분 검색
    List<PersonEntity> findByNameContainingIgnoreCase(String keyword); //0911생성 joo
}