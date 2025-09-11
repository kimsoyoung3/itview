package com.example.itview_spring.Controller.Collection;

import java.util.NoSuchElementException;

import org.aspectj.apache.bcel.generic.ClassGen;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.itview_spring.Config.CustomUserDetails;
import com.example.itview_spring.DTO.CollectionCreateDTO;
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
        System.out.println(dto.getTitle() + " " + dto.getDescription());
        collectionService.createCollection(user.getId(), dto);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNoSuchElementException(NoSuchElementException ex) {
        return ResponseEntity.status(404).body(ex.getMessage());
    }
}