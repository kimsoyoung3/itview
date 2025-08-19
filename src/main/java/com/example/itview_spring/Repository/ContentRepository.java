package com.example.itview_spring.Repository;

import com.example.itview_spring.Entity.ContentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentRepository extends JpaRepository<ContentEntity, Integer> {
    // 기본적인 CRUD는 JpaRepository에서 제공됨
}
