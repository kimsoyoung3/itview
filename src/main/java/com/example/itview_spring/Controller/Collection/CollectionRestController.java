package com.example.itview_spring.Controller.Collection;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.itview_spring.Config.CustomUserDetails;
import com.example.itview_spring.DTO.CollectionFormDTO;
import com.example.itview_spring.DTO.CollectionResponseDTO;
import com.example.itview_spring.DTO.CollectionToAddDTO;
import com.example.itview_spring.DTO.ContentResponseDTO;
import com.example.itview_spring.DTO.ReplyDTO;
import com.example.itview_spring.DTO.TextDTO;
import com.example.itview_spring.Service.CollectionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/collection")
public class CollectionRestController {

    private final CollectionService collectionService;

    // 컬렉션 생성
    @PostMapping
    public ResponseEntity<Integer> createCollection(@AuthenticationPrincipal CustomUserDetails user, @RequestBody CollectionFormDTO dto) {
        return ResponseEntity.ok(collectionService.createCollection(user.getId(), dto));
    }

    // 컬렉션 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<CollectionResponseDTO> getCollectionDetail(@PathVariable Integer id) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        Integer loginUserId = 0;
        if (auth.getPrincipal() != "anonymousUser") {
            loginUserId = ((CustomUserDetails) auth.getPrincipal()).getId();
        }
        CollectionResponseDTO collection = collectionService.getCollectionDetail(loginUserId, id);
        return ResponseEntity.ok(collection);
    }

    // 컬렉션에 추가 조회
    @GetMapping("/add/{contentId}")
    public ResponseEntity<Page<CollectionToAddDTO>> getCollectionAddItems(@PathVariable Integer contentId,
                                                                          @AuthenticationPrincipal CustomUserDetails user,
                                                                          @PageableDefault(page=1) Pageable pageable) {
        Page<CollectionToAddDTO> contents = collectionService.getCollectionsToAdd(user.getId(), contentId, pageable.getPageNumber());
        return ResponseEntity.ok(contents);
    }

    // 컬렉션에 추가
    @PostMapping("/add/{contentId}")
    public ResponseEntity<Void> addContentToCollection(@AuthenticationPrincipal CustomUserDetails user, @PathVariable Integer contentId, @RequestBody List<Integer> collectionIds) {
        collectionService.addContentToCollection(user.getId(), contentId, collectionIds);
        return ResponseEntity.ok().build();
    }

    // 컬렉션 아이템 페이징 조회
    @GetMapping("/{id}/items")
    public ResponseEntity<Page<ContentResponseDTO>> getCollectionItems(@PathVariable Integer id,
                                                                       @PageableDefault(page=1) Pageable pageable) {
        Page<ContentResponseDTO> collections = collectionService.getCollectionItems(id, pageable.getPageNumber());
        return ResponseEntity.ok(collections);
    }

    // 컬렉션 수정 조회
    @GetMapping("/{id}/edit")
    public ResponseEntity<CollectionFormDTO> getCollectionForEdit(@AuthenticationPrincipal CustomUserDetails user, @PathVariable Integer id) {
        CollectionFormDTO collection = collectionService.getCollectionForm(user.getId(), id);
        return ResponseEntity.ok(collection);
    }

    // 컬렉션 수정
    @PutMapping("/{id}")
    public ResponseEntity<Void> editCollection(@AuthenticationPrincipal CustomUserDetails user, @PathVariable Integer id, @RequestBody CollectionFormDTO dto) {
        collectionService.editCollection(user.getId(), id, dto);
        return ResponseEntity.ok().build();
    }

    // 컬렉션 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCollection(@AuthenticationPrincipal CustomUserDetails user, @PathVariable Integer id) {
        collectionService.deleteCollection(user.getId(), id);
        return ResponseEntity.ok().build();
    }

    // 컬렉션 좋아요
    @PostMapping("/{id}/like")
    public ResponseEntity<Void> likeCollection(@AuthenticationPrincipal CustomUserDetails user, @PathVariable Integer id) {
        collectionService.addLike(user.getId(), id);
        return ResponseEntity.ok().build();
    }
    
    // 컬렉션 좋아요 취소
    @DeleteMapping("/{id}/like")
    public ResponseEntity<Void> unlikeCollection(@AuthenticationPrincipal CustomUserDetails user, @PathVariable Integer id) {
        collectionService.removeLike(user.getId(), id);
        return ResponseEntity.ok().build();
    }

    // 댓글 페이징 조회
    @GetMapping("/{id}/reply")
    public ResponseEntity<Page<ReplyDTO>> getCollectionReplies(@PathVariable Integer id, @PageableDefault(page=1) Pageable pageable) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        Integer userId = 0;
        if (auth.getPrincipal() != "anonymousUser") {
            userId = ((CustomUserDetails) auth.getPrincipal()).getId();
        }
        Page<ReplyDTO> replies = collectionService.getCollectionReplies(userId, id, pageable.getPageNumber());
        return ResponseEntity.ok(replies);
    }

    // 댓글 작성
    @PostMapping("/{id}/reply")
    public ResponseEntity<ReplyDTO> insertReply(@AuthenticationPrincipal CustomUserDetails user, @PathVariable Integer id, @RequestBody TextDTO text) {
        ReplyDTO newReply = collectionService.insertReply(user.getId(), id, text.getText());
        return ResponseEntity.ok(newReply);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNoSuchElementException(NoSuchElementException ex) {
        return ResponseEntity.status(404).body(ex.getMessage());
    }

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<String> handleSecurityException(SecurityException ex) {
        return ResponseEntity.status(403).body(ex.getMessage());
    }
}