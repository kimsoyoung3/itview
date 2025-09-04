package com.example.itview_spring.Controller.Reply;

import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.itview_spring.Config.CustomUserDetails;
import com.example.itview_spring.DTO.TextDTO;
import com.example.itview_spring.Service.ReplyService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reply")
@RequiredArgsConstructor
public class ReplyRestController {

    private final ReplyService replyService;

    // 댓글에 좋아요 등록
    @PostMapping("/{id}/like")
    public ResponseEntity<Void> likeReply(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable("id") Integer id) {
        replyService.likeReply(userDetails.getId(), id);
        return ResponseEntity.ok().build();
    }

    // 댓글에 좋아요 취소
    @DeleteMapping("/{id}/like")
    public ResponseEntity<Void> unlikeReply(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable("id") Integer id) {
        replyService.unlikeReply(userDetails.getId(), id);
        return ResponseEntity.ok().build();
    }

    // 댓글 수정
    @PutMapping("/{id}")
    public ResponseEntity<Void> modifyReply(@PathVariable("id") Integer id, @RequestBody TextDTO newText) {
        Boolean success = replyService.modifyReply(id, newText.getText());
        if (success) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReply(@PathVariable("id") Integer id) {
        Boolean success = replyService.deleteReply(id);
        if (success) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNoSuchElementException(NoSuchElementException ex) {
        return ResponseEntity.status(404).body(ex.getMessage());
    }
}