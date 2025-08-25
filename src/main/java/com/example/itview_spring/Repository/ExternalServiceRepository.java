package com.example.itview_spring.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.itview_spring.DTO.ExternalServiceDTO;
import com.example.itview_spring.Entity.ExternalServiceEntity;

@Repository
public interface ExternalServiceRepository extends JpaRepository<ExternalServiceEntity, Integer> {

    // 컨텐츠 ID로 외부 서비스 정보 조회
    @Query("""
            SELECT new com.example.itview_spring.DTO.ExternalServiceDTO(
                e.id,
                e.type,
                e.href
            )
            FROM ExternalServiceEntity e
            WHERE e.content.id = :contentId
        """)
    List<ExternalServiceDTO> findByContentId(@Param("contentId") Integer contentId);
}
