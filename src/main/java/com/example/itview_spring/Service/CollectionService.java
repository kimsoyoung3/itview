package com.example.itview_spring.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import com.example.itview_spring.DTO.*;
import com.example.itview_spring.Entity.*;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.itview_spring.Constant.ActivityLogType;
import com.example.itview_spring.Constant.NotiType;
import com.example.itview_spring.Constant.Replyable;
import com.example.itview_spring.Repository.ActivityLogRepository;
import com.example.itview_spring.Repository.CollectionRepository;
import com.example.itview_spring.Repository.ContentRepository;
import com.example.itview_spring.Repository.LikeRepository;
import com.example.itview_spring.Repository.NotificationRepository;
import com.example.itview_spring.Repository.ReplyRepository;
import com.example.itview_spring.Repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CollectionService {
    
    private final CollectionRepository collectionRepository;
    private final ActivityLogRepository activityLogRepository;
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final ReplyRepository replyRepository;
    private final ContentRepository contentRepository;
    private final NotificationService notificationService;
    private final ModelMapper modelMapper;

    // 컬렉션 생성
    public Integer createCollection(Integer userId, CollectionFormDTO dto) {
        CollectionEntity collection = new CollectionEntity();
        collection.setUser(userRepository.findById(userId).get());
        collection.setTitle(dto.getTitle());
        collection.setDescription(dto.getDescription());

        for (int i = dto.getContentId().size() - 1; i >= 0; i--) {
            ContentEntity content = contentRepository.findById(dto.getContentId().get(i)).orElseThrow(() -> new NoSuchElementException("존재하지 않는 컨텐츠입니다."));
            CollectionItemEntity item = new CollectionItemEntity();
            item.setCollection(collection);
            item.setContent(content);
            collection.getItems().add(item);
        }
        
        collectionRepository.save(collection);

        ActivityLogEntity activityLog = new ActivityLogEntity();
        activityLog.setUser(collection.getUser());
        activityLog.setType(ActivityLogType.COLLECTION);
        activityLog.setReferenceId(collection.getId());
        activityLog.setTimestamp(LocalDateTime.now());
        activityLogRepository.save(activityLog);

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

    // 컬렉션에 추가 조회
    public Page<CollectionToAddDTO> getCollectionsToAdd(Integer userId, Integer contentId, Integer page) {
        if (!userRepository.existsById(userId)) {
            throw new NoSuchElementException("존재하지 않는 유저입니다.");
        }
        if (!contentRepository.existsById(contentId)) {
            throw new NoSuchElementException("존재하지 않는 컨텐츠입니다.");
        }
        Pageable pageable = PageRequest.of(page - 1, 12);
        Page<CollectionToAddDTO> collections = collectionRepository.findCollectionsToAdd(userId, contentId, pageable);
        for (CollectionToAddDTO dto : collections) {
            List<String> posters = collectionRepository.findCollectionPosters(dto.getCollection().getId());
            dto.getCollection().setPoster(posters);
        }
        return collections;
    }

    // 컬렉션에 추가
    public void addContentToCollection(Integer userId, Integer contentId, List<Integer> collectionIds) {
        if (!userRepository.existsById(userId)) {
            throw new NoSuchElementException("존재하지 않는 유저입니다.");
        }
        ContentEntity content = contentRepository.findById(contentId).orElseThrow(() -> new NoSuchElementException("존재하지 않는 컨텐츠입니다."));
        List<CollectionEntity> collections = collectionRepository.findAllById(collectionIds);
        for (CollectionEntity collection : collections) {
            if (!collection.getUser().getId().equals(userId)) {
                throw new SecurityException("본인의 컬렉션에만 추가할 수 있습니다.");
            }
            // 이미 추가된 컨텐츠는 건너뜀
            if (collection.getItems().stream().map(item -> item.getContent().getId()).collect(Collectors.toList()).contains(contentId)) {
                continue;
            }
            CollectionItemEntity item = new CollectionItemEntity();
            item.setCollection(collection);
            item.setContent(content);
            collection.getItems().add(item);
            collection.setUpdatedAt(LocalDateTime.now());
            collectionRepository.save(collection);

            ActivityLogEntity activityLog = activityLogRepository.findByReferenceIdAndType(collection.getId(), ActivityLogType.COLLECTION);
            if (activityLog != null) {
                activityLog.setTimestamp(LocalDateTime.now());
                activityLog.setIsUpdate(true);
                activityLogRepository.save(activityLog);
            } else {
                activityLog = new ActivityLogEntity();
                activityLog.setUser(collection.getUser());
                activityLog.setType(ActivityLogType.COLLECTION);
                activityLog.setReferenceId(collection.getId());
                activityLog.setTimestamp(LocalDateTime.now());
                activityLog.setIsUpdate(true);
                activityLogRepository.save(activityLog);
            }
        }
    }

    // 컬렉션 아이템 페이징 조회
    public Page<ContentResponseDTO> getCollectionItems(Integer id, Integer page) {
        if (!collectionRepository.existsById(id)) {
            throw new NoSuchElementException("존재하지 않는 컬렉션입니다.");
        }
        Pageable pageable = PageRequest.of(page - 1, 12);
        return collectionRepository.findCollectionItemsById(id, pageable);
    }

    // 컬렉션 수정 조회
    public CollectionFormDTO getCollectionForm(Integer userId, Integer id) {
        CollectionEntity collection = collectionRepository.findById(id).orElseThrow(() -> new NoSuchElementException("존재하지 않는 컬렉션입니다."));
        if (!collection.getUser().getId().equals(userId)) {
            throw new SecurityException("본인의 컬렉션만 수정할 수 있습니다.");
        }
        CollectionFormDTO form = collectionRepository.findCollectionFormById(id);
        form.setContentId(collectionRepository.findCollectionItemIdsById(id));
        return form;
    }

    // 컬렉션 수정
    public void editCollection(Integer userId, Integer id, CollectionFormDTO dto) {
        CollectionEntity collection = collectionRepository.findById(id).orElseThrow(() -> new NoSuchElementException("존재하지 않는 컬렉션입니다."));
        if (!collection.getUser().getId().equals(userId)) {
            throw new SecurityException("본인의 컬렉션만 수정할 수 있습니다.");
        }
        collection.setTitle(dto.getTitle());
        collection.setDescription(dto.getDescription());

        // 기존 아이템 삭제
        collection.getItems().clear();

        // 아이템 덮어쓰기
        for (int i = dto.getContentId().size() - 1; i >= 0; i--) {
            ContentEntity content = contentRepository.findById(dto.getContentId().get(i)).orElseThrow(() -> new NoSuchElementException("존재하지 않는 컨텐츠입니다."));
            CollectionItemEntity item = new CollectionItemEntity();
            item.setCollection(collection);
            item.setContent(content);
            collection.getItems().add(item);
            if (i == 0) {
                collection.setUpdatedAt(LocalDateTime.now());
            }
        }
        
        collectionRepository.save(collection);

        ActivityLogEntity activityLog = activityLogRepository.findByReferenceIdAndType(collection.getId(), ActivityLogType.COLLECTION);
        if (activityLog != null) {
            activityLog.setTimestamp(LocalDateTime.now());
            activityLog.setIsUpdate(true);
            activityLogRepository.save(activityLog);
        } else {
            activityLog = new ActivityLogEntity();
            activityLog.setUser(collection.getUser());
            activityLog.setType(ActivityLogType.COLLECTION);
            activityLog.setReferenceId(collection.getId());
            activityLog.setTimestamp(LocalDateTime.now());
            activityLog.setIsUpdate(true);
            activityLogRepository.save(activityLog);
        }
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
        notificationRepository.deleteByTargetIdAndTargetType(id, Replyable.COLLECTION);
        activityLogRepository.deleteByReferenceIdAndType(id, ActivityLogType.COLLECTION);
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

        // 알림 생성
        CollectionEntity collection = collectionRepository.findById(id).orElseThrow(() -> new NoSuchElementException("존재하지 않는 컬렉션입니다."));
        if (!collection.getUser().getId().equals(userId)) { // 본인에게는 알림을 보내지 않음
            NotificationEntity notification = new NotificationEntity();
            notification.setUser(collection.getUser());
            notification.setActor(userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("존재하지 않는 유저입니다")));
            notification.setType(NotiType.LIKE);
            notification.setTargetType(Replyable.COLLECTION);
            notification.setTargetId(id);
            notificationRepository.save(notification);
            // 실시간 알림 전송
            notificationService.sendNotification(collection.getUser().getId());
        }
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

        // 알림 생성

        // 해당 컬렉션의 작성자에게 알림 전송
        CollectionEntity collection = collectionRepository.findById(id).orElseThrow(() -> new NoSuchElementException("존재하지 않는 컬렉션입니다."));
        if (!collection.getUser().getId().equals(userId) ) { // 본인 및 작성자에게는 알림을 보내지 않음
            NotificationEntity notification = new NotificationEntity();
            notification.setUser(collection.getUser());
            notification.setActor(userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("존재하지 않는 유저입니다")));
            notification.setType(NotiType.REPLY);
            notification.setTargetType(Replyable.COLLECTION);
            notification.setTargetId(id);
            notificationRepository.save(notification);
            // 실시간 알림 전송
            notificationService.sendNotification(collection.getUser().getId());
        }

        // 해당 컬렉션에 댓글을 남긴 모든 유저에게 알림 전송
        List<Integer> recipientIds = collectionRepository.findAllReplyUserIdsByCollectionId(id);
        for (Integer recipientId : recipientIds) {
            if (!recipientId.equals(userId) && !recipientId.equals(collection.getUser().getId())) { // 본인 및 컬렉션 작성자에게는 알림을 보내지 않음
                NotificationEntity notification = new NotificationEntity();
                notification.setUser(userRepository.findById(recipientId).orElseThrow(() -> new NoSuchElementException("존재하지 않는 유저입니다")));
                notification.setActor(userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("존재하지 않는 유저입니다")));
                notification.setType(NotiType.REPLY);
                notification.setTargetType(Replyable.COLLECTION);
                notification.setTargetId(id);
                notificationRepository.save(notification);
                // 실시간 알림 전송
                notificationService.sendNotification(recipientId);
            }
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

    // 관리자 페이지 - 컬렉션 상세 조회
    public AdminCollectionDTO getCollectionDetail(Integer collectionId) {
        CollectionEntity collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new NoSuchElementException("컬렉션이 존재하지 않습니다."));

        // UserEntity를 UserProfileDTO로 변환하는 로직 수정
        UserProfileDTO userProfileDTO = null;
        if (collection.getUser() != null) {
            userProfileDTO = new UserProfileDTO(
                    collection.getUser().getId(), // User id
                    collection.getUser().getNickname(), // 닉네임
                    collection.getUser().getIntroduction(), // 자기소개
                    collection.getUser().getProfile() // 프로필 사진 URL
            );
        }

        // Entity를 DTO로 변환
        return new AdminCollectionDTO(
                collection.getId(),
                collection.getTitle(),
                collection.getDescription(),
                collection.getUpdatedAt(),
                userProfileDTO
        );
    }

    // 관리자 페이지 - 컬렉션에 속한 콘텐츠 목록 조회 (페이지네이션)
    public Page<AdminContentDTO> getContentsByCollectionId(Integer collectionId, Pageable pageable) {
        if (!collectionRepository.existsById(collectionId)) {
            throw new NoSuchElementException("컬렉션이 존재하지 않습니다.");
        }

        Page<ContentEntity> contentsPage = collectionRepository.findContentsByCollectionId(collectionId, pageable);

        return contentsPage.map(content -> new AdminContentDTO(
                content.getId(),
                content.getTitle(),
                content.getPoster()
        ));
    }

    // 관리자 페이지 - 컬렉션 수정
    public AdminCollectionDTO updateCollection(int id, AdminCollectionDTO dto) {
        CollectionEntity collectionEntity = collectionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 컬렉션을 찾을 수 없습니다. id: " + id));

        // DTO -> 엔티티 (변경을 원하는 필드만 매핑)
        collectionEntity.setTitle(dto.getTitle());
        collectionEntity.setDescription(dto.getDescription());

        // 엔티티 -> DTO (최종 반환용)
        return modelMapper.map(collectionEntity, AdminCollectionDTO.class);
    }
}
