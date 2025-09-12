package com.example.itview_spring.Service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.itview_spring.DTO.CollectionCreateDTO;
import com.example.itview_spring.Entity.CollectionEntity;
import com.example.itview_spring.Entity.CollectionItemEntity;
import com.example.itview_spring.Entity.ContentEntity;
import com.example.itview_spring.Repository.CollectionRepository;
import com.example.itview_spring.Repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CollectionService {
    
    private final CollectionRepository collectionRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    // 컬렉션 생성
    public void createCollection(Integer userId, CollectionCreateDTO dto) {
        CollectionEntity collection = new CollectionEntity();
        collection.setUser(userRepository.findById(userId).get());
        collection.setTitle(dto.getTitle());
        collection.setDescription(dto.getDescription());
        
        for (var content : dto.getContents()) {
            CollectionItemEntity item = new CollectionItemEntity();
            item.setCollection(collection);
            item.setContent(modelMapper.map(content, ContentEntity.class));
            collection.getItems().add(item);
        }
        
        collectionRepository.save(collection);
    }
}
