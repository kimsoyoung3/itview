package com.example.itview_spring.Controller.Comment;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.itview_spring.Config.CustomUserDetails;
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
    public void likeComment(@PathVariable("id") Integer commentId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            commentService.likeComment(userDetails.getId(), commentId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 좋아요 취소
    @DeleteMapping("/{id}/like")
    public void unlikeComment(@PathVariable("id") Integer commentId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            commentService.unlikeComment(commentId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}