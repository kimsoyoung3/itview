package com.example.itview_spring.Controller.Comment;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.itview_spring.Config.CustomUserDetails;
import com.example.itview_spring.DTO.TextDTO;
import com.example.itview_spring.Service.CommentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/comment")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RequiredArgsConstructor
public class CommentRestController {
    
    private final CommentService commentService;

    // 좋아요 등록
    @PostMapping("/{id}/like")
    public ResponseEntity<Void> likeComment(@PathVariable("id") Integer commentId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            commentService.likeComment(userDetails.getId(), commentId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    // 좋아요 취소
    @DeleteMapping("/{id}/like")
    public ResponseEntity<Void> unlikeComment(@PathVariable("id") Integer commentId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            commentService.unlikeComment(commentId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    // 댓글 등록
    @PostMapping("/{id}/reply")
    public ResponseEntity<Void> addReply(@PathVariable("id") Integer commentId, @AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody TextDTO textDTO) {
        try {
            commentService.addReply(userDetails.getId(), commentId, textDTO.getText());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}