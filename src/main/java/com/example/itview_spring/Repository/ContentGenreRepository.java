package com.example.itview_spring.Repository;

import com.example.itview_spring.Entity.ContentEntity;
import com.example.itview_spring.Entity.ContentGenreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContentGenreRepository extends JpaRepository<ContentGenreEntity, Integer> {

    // 특정 컨텐츠 ID로 장르 리스트 조회
    List<ContentGenreEntity> findByContent (ContentEntity content);

    // 삭제 시: 특정 컨텐츠에 연관된 모든 장르 삭제
    void deleteByContent (ContentEntity content);
}