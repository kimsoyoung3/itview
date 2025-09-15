package com.example.itview_spring.Service;

import java.util.List;
import java.util.NoSuchElementException;

import com.example.itview_spring.DTO.AdminCollectionDTO;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.itview_spring.Constant.Replyable;
import com.example.itview_spring.DTO.CollectionFormDTO;
import com.example.itview_spring.DTO.CollectionResponseDTO;
import com.example.itview_spring.DTO.ContentResponseDTO;
import com.example.itview_spring.DTO.ReplyDTO;
import com.example.itview_spring.Entity.CollectionEntity;
import com.example.itview_spring.Entity.CollectionItemEntity;
import com.example.itview_spring.Entity.ContentEntity;
import com.example.itview_spring.Entity.ReplyEntity;
import com.example.itview_spring.Repository.CollectionRepository;
import com.example.itview_spring.Repository.ContentRepository;
import com.example.itview_spring.Repository.LikeRepository;
import com.example.itview_spring.Repository.ReplyRepository;
import com.example.itview_spring.Repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CollectionService {
    
    private final CollectionRepository collectionRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final ReplyRepository replyRepository;
    private final ContentRepository contentRepository;

    // 컬렉션 생성
    public Integer createCollection(Integer userId, CollectionFormDTO dto) {
        CollectionEntity collection = new CollectionEntity();
        collection.setUser(userRepository.findById(userId).get());
        collection.setTitle(dto.getTitle());
        collection.setDescription(dto.getDescription());

        for (Integer contentId : dto.getContentId()) {
            ContentEntity content = contentRepository.findById(contentId).orElseThrow(() -> new NoSuchElementException("존재하지 않는 컨텐츠입니다."));
            CollectionItemEntity item = new CollectionItemEntity();
            item.setCollection(collection);
            item.setContent(content);
            collection.getItems().add(item);
        }
        
        collectionRepository.save(collection);
        return collection.getId();
    }

    // 컬렉션 상세 조회
    public CollectionResponseDTO getCollectionDetail(Integer loginUserId, Integer id) {
        if (!collectionRepository.existsById(id)) {
            throw new NoSuchElementException("존재하지 않는 컬렉션입니다.");
        }
        CollectionResponseDTO collection = collectionRepository.findCollectionById(loginUserId, id);
        collection.setPoster(getCollectionPosters(id));
        return collection;
    }

    // 컬렉션 아이템 페이징 조회
    public Page<ContentResponseDTO> getCollectionItems(Integer id, Integer page) {
        if (!collectionRepository.existsById(id)) {
            throw new NoSuchElementException("존재하지 않는 컬렉션입니다.");
        }
        Pageable pageable = PageRequest.of(page - 1, 12);
        return collectionRepository.findCollectionItemsById(id, pageable);
    }

    // 컬렉션 삭제
    public void deleteCollection(Integer userId, Integer id) {
        CollectionEntity collection = collectionRepository.findById(id).orElseThrow(() -> new NoSuchElementException("존재하지 않는 컬렉션입니다."));
        if (!collection.getUser().getId().equals(userId)) {
            throw new SecurityException("본인의 컬렉션만 삭제할 수 있습니다.");
        }
        likeRepository.deleteByTargetIdAndTargetType(id, Replyable.COLLECTION);
        List<Integer> replyIds = replyRepository.findAllByCollectionId(id);
        for (Integer replyId : replyIds) {
            likeRepository.deleteByTargetIdAndTargetType(replyId, Replyable.REPLY);
        }
        replyRepository.deleteByTargetIdAndTargetType(id, Replyable.COLLECTION);
        collectionRepository.deleteById(id);
    }

    // 컬렉션에 좋아요 추가
    public void addLike(Integer userId, Integer id) {
        if (!collectionRepository.existsById(id)) {
            throw new NoSuchElementException("존재하지 않는 컬렉션입니다.");
        }
        if (likeRepository.existsByUserIdAndTargetIdAndTargetType(userId, id, Replyable.COLLECTION)) {
            return;
        }
        likeRepository.likeTarget(userId, id, Replyable.COLLECTION);
    }

    // 컬렉션에 좋아요 제거
    public void removeLike(Integer userId, Integer id) {
        if (!collectionRepository.existsById(id)) {
            throw new NoSuchElementException("존재하지 않는 컬렉션입니다.");
        }
        likeRepository.unlikeTarget(userId, id, Replyable.COLLECTION);
    }

    // 컬렉션 댓글 페이징 조회
    public Page<ReplyDTO> getCollectionReplies(Integer userId, Integer id, Integer page) {
        if (!collectionRepository.existsById(id)) {
            throw new NoSuchElementException("존재하지 않는 컬렉션입니다.");
        }
        Pageable pageable = PageRequest.of(page - 1, 10);
        return replyRepository.findRepliesByTargetId(userId, id, Replyable.COLLECTION, pageable);
    }

    // 컬렉션에 댓글 작성
    public ReplyDTO insertReply(Integer userId, Integer id, String text) {
        if (!collectionRepository.existsById(id)) {
            throw new NoSuchElementException("존재하지 않는 컬렉션입니다.");
        }
        ReplyEntity reply = new ReplyEntity();
        reply.setUser(userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found")));
        reply.setTargetId(id);
        reply.setTargetType(Replyable.COLLECTION);
        reply.setText(text);
        replyRepository.save(reply);
        ReplyDTO newReply = replyRepository.findReplyDTOById(userId, reply.getId());
        if (newReply == null) {
            throw new RuntimeException("Failed to create reply");
        }
        return newReply;
    }

    // 컬렉션 썸네일용 포스터 이미지 조회
    public List<String> getCollectionPosters(Integer id) {
        return collectionRepository.findCollectionPosters(id);
    }

    // 관리자 페이지 - 컬렉션 목록 조회
    public Page<AdminCollectionDTO> list(String keyword, Pageable pageable) {
        Page<CollectionEntity> collectionsPage;

        if (keyword != null && !keyword.trim().isEmpty()) {
            // 검색 키워드가 있으면 제목 또는 유저 닉네임으로 검색
            collectionsPage = collectionRepository.findByTitleOrUserNicknameContaining(keyword, pageable);
        } else {
            // 검색 키워드가 없으면 모든 컬렉션 조회
            collectionsPage = collectionRepository.findAll(pageable);
        }

        // 엔티티 페이지를 DTO 페이지로 변환
        return collectionsPage.map(collection -> modelMapper.map(collection, AdminCollectionDTO.class));
    }

    // 관리자 페이지 - 컬렉션 삭제
    public void delete(int id) {
        collectionRepository.deleteById(id);
    }
}
