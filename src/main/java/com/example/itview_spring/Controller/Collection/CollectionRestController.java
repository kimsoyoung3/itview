package com.example.itview_spring.Controller.Collection;

import java.util.NoSuchElementException;

import org.aspectj.apache.bcel.generic.ClassGen;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.itview_spring.Config.CustomUserDetails;
import com.example.itview_spring.DTO.CollectionCreateDTO;
import com.example.itview_spring.DTO.CollectionResponseDTO;
import com.example.itview_spring.DTO.ContentResponseDTO;
import com.example.itview_spring.Service.CollectionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/collection")
public class CollectionRestController {

    private final CollectionService collectionService;

    // 컬렉션 생성
    @PostMapping
    public ResponseEntity<Void> createCollection(@AuthenticationPrincipal CustomUserDetails user, @RequestBody CollectionCreateDTO dto) {
        collectionService.createCollection(user.getId(), dto);
        return ResponseEntity.ok().build();
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

    // 컬렉션 아이템 페이징 조회
    @GetMapping("/{id}/items")
    public ResponseEntity<Page<ContentResponseDTO>> getCollectionItems(@PathVariable Integer id,
                                                                       @PageableDefault(page=1) Pageable pageable) {
        Page<ContentResponseDTO> collections = collectionService.getCollectionItems(id, pageable.getPageNumber());
        return ResponseEntity.ok(collections);
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

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNoSuchElementException(NoSuchElementException ex) {
        return ResponseEntity.status(404).body(ex.getMessage());
    }
}