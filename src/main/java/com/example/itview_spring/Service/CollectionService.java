package com.example.itview_spring.Service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.itview_spring.DTO.CollectionCreateDTO;
import com.example.itview_spring.Entity.CollectionEntity;
import com.example.itview_spring.Repository.CollectionRepository;
import com.example.itview_spring.Repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CollectionService {
    
    private final CollectionRepository collectionRepository;
    private final UserRepository userRepository;

    // 컬렉션 생성
    public void createCollection(Integer userId, CollectionCreateDTO dto) {
        CollectionEntity collection = new CollectionEntity();
        collection.setUser(userRepository.findById(userId).get());
        collection.setTitle(dto.getTitle());
        collection.setDescription(dto.getDescription());
        collectionRepository.save(collection);
    }
}
