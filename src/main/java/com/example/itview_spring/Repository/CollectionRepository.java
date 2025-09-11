package com.example.itview_spring.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.itview_spring.Entity.CollectionEntity;

public interface CollectionRepository extends JpaRepository<CollectionEntity, Long> {
    
}
