package com.example.itview_spring.Service;

import java.util.List;
import java.util.NoSuchElementException;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.itview_spring.DTO.CollectionCreateDTO;
import com.example.itview_spring.DTO.CollectionResponseDTO;
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

    // 컬렉션 상세 조회
    public CollectionResponseDTO getCollectionDetail(Integer loginUserId, Integer id) {
        if (!collectionRepository.existsById(id)) {
            throw new NoSuchElementException("존재하지 않는 컬렉션입니다.");
        }
        return collectionRepository.findCollectionById(loginUserId, id);
    }

    // 컬렉션 썸네일용 포스터 이미지 조회
    public List<String> getCollectionPosters(Integer id) {
        return collectionRepository.findCollectionPosters(id);
    }
}
