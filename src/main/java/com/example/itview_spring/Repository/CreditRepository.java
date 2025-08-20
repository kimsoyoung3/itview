package com.example.itview_spring.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.itview_spring.DTO.CreditDTO;
import com.example.itview_spring.Entity.CreditEntity;

public interface CreditRepository extends JpaRepository<CreditEntity, Integer> {
    
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
}
