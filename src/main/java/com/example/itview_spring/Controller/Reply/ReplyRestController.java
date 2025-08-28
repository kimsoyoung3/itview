package com.example.itview_spring.Controller.Reply;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.itview_spring.Config.CustomUserDetails;
import com.example.itview_spring.Service.ReplyService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reply")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RequiredArgsConstructor
public class ReplyRestController {

    private final ReplyService replyService;

    // 댓글에 좋아요 등록
    @PostMapping("/{id}/like")
    public ResponseEntity<Void> likeReply(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable("id") Integer id) {
        try {
            replyService.likeReply(userDetails.getId(), id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 댓글에 좋아요 취소
    @DeleteMapping("/{id}/like")
    public ResponseEntity<Void> unlikeReply(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable("id") Integer id) {
        try {
            replyService.unlikeReply(userDetails.getId(), id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}