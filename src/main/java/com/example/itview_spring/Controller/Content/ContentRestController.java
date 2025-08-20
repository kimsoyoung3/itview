package com.example.itview_spring.Controller.Content;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.itview_spring.DTO.ContentResponseDTO;
import com.example.itview_spring.Service.ContentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/content")
@RequiredArgsConstructor
public class ContentRestController {

    private final ContentService contentService;

    @GetMapping("/{id}")
    public ResponseEntity<ContentResponseDTO> getContentDetail(@PathVariable("id") Integer id) {
        ContentResponseDTO contentDetail = contentService.getContentDetail(id);
        return ResponseEntity.ok(contentDetail);
    }
}
