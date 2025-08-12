package com.example.itview_spring.Repository;

import com.example.itview_spring.Entity.ContentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentRepository extends JpaRepository<ContentEntity, Integer> {
}
