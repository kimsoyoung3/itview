package com.example.itview_spring.Repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.itview_spring.Constant.ContentType;
import com.example.itview_spring.DTO.CreditDTO;
import com.example.itview_spring.DTO.WorkDTO;
import com.example.itview_spring.DTO.WorkDomainDTO;
import com.example.itview_spring.Entity.CreditEntity;

public interface CreditRepository extends JpaRepository<CreditEntity, Integer> {
    
    // 컨텐츠 ID로 크레딧 정보 조회
    @Query("""
            SELECT new com.example.itview_spring.DTO.CreditDTO(
                c.id,
                new com.example.itview_spring.DTO.PersonDTO(p.id, p.name, p.profile, p.job),
                c.characterName,
                c.department,
                c.role
            )
            FROM CreditEntity c
            JOIN c.person p
            WHERE c.content.id = :contentId
        """)
    Page<CreditDTO> findByContentId(Pageable pageable, @Param("contentId") Integer contentId);

    // 인물의 작품 참여 분야 조회
    @Query("""
            SELECT new com.example.itview_spring.DTO.WorkDomainDTO(
                con.contentType,
                cre.department
            )
            FROM CreditEntity cre
            JOIN ContentEntity con ON cre.content.id = con.id
            WHERE cre.person.id = :personId
            GROUP BY con.contentType, cre.department
            ORDER BY
                CASE con.contentType
                    WHEN 'MOVIE' THEN 1
                    WHEN 'SERIES' THEN 2
                    WHEN 'BOOK' THEN 3
                    WHEN 'WEBTOON' THEN 4
                    WHEN 'RECORD' THEN 5
                END,
                count(cre.id) DESC
            """)
    List<WorkDomainDTO> findWorkDomainsByPersonId(@Param("personId") Integer personId);

    // 분야별 페이징 조회
    @Query("""
            SELECT new com.example.itview_spring.DTO.WorkDTO(
                con.id,
                con.poster,
                con.contentType,
                con.title,
                con.releaseDate,
                cre.department,
                cre.role,
                (SELECT AVG(r.score) FROM RatingEntity r WHERE r.content.id = con.id)
            )
            FROM CreditEntity cre
            JOIN ContentEntity con ON cre.content.id = con.id
            WHERE cre.person.id = :personId AND con.contentType = :contentType AND cre.department = :department
            ORDER BY con.releaseDate DESC
            """)
    Page<WorkDTO> findWorkDTOPage(Pageable pageable, @Param("personId") Integer personId, @Param("contentType") ContentType contentType, @Param("department") String department);
}