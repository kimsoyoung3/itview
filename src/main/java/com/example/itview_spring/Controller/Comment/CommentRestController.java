package com.example.itview_spring.Controller.Comment;

import java.util.NoSuchElementException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import com.example.itview_spring.DTO.CommentAndContentDTO;
import com.example.itview_spring.DTO.ReplyDTO;
import com.example.itview_spring.DTO.TextDTO;
import com.example.itview_spring.Service.CommentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentRestController {
    
    private final CommentService commentService;
    
    // 컨텐츠 코멘트 수정
    @PutMapping("/{Id}")
    public ResponseEntity<Void> putContentComment(@PathVariable("Id") Integer commentId,
                                                  @AuthenticationPrincipal CustomUserDetails userDetails,
                                                  @RequestBody TextDTO textDTO) {
        commentService.updateComment(commentId, textDTO.getText());
        return ResponseEntity.ok().build();
    }

    // 컨텐츠 코멘트 삭제
    @DeleteMapping("/{Id}")
    public ResponseEntity<Void> deleteContentComment(@PathVariable("Id") Integer commentId,
                                                     @AuthenticationPrincipal CustomUserDetails userDetails) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok().build();
    }

    // 코멘트 + 컨텐츠 조회
    @GetMapping("/{id}")
    public ResponseEntity<CommentAndContentDTO> getCommentAndContent(@PathVariable("id") Integer commentId) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        Integer userId = 0;
        if (auth.getPrincipal() != "anonymousUser") {
            userId = ((CustomUserDetails) auth.getPrincipal()).getId();
        }
        CommentAndContentDTO commentAndContent = commentService.getCommentAndContentDTO(commentId, userId);
        return ResponseEntity.ok(commentAndContent);
    }

    // 좋아요 등록
    @PostMapping("/{id}/like")
    public ResponseEntity<Void> likeComment(@PathVariable("id") Integer commentId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        commentService.likeComment(userDetails.getId(), commentId);
        return ResponseEntity.ok().build();
    }

    // 좋아요 취소
    @DeleteMapping("/{id}/like")
    public ResponseEntity<Void> unlikeComment(@PathVariable("id") Integer commentId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        commentService.unlikeComment(userDetails.getId(), commentId);
        return ResponseEntity.ok().build();
    }

    // 댓글 등록
    @PostMapping("/{id}/reply")
    public ResponseEntity<ReplyDTO> addReply(@PathVariable("id") Integer commentId, @AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody TextDTO textDTO) {
        ReplyDTO newReply = commentService.addReply(userDetails.getId(), commentId, textDTO.getText());
        return ResponseEntity.ok(newReply);
    }

    // 코멘트의 댓글 페이징 조회
    @GetMapping("/{id}/reply")
    public ResponseEntity<Page<ReplyDTO>> getCommentReplies(@PathVariable("id") Integer commentId, @PageableDefault(page = 1) Pageable pageable) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        Integer userId = 0;
        if (auth.getPrincipal() != "anonymousUser") {
            userId = ((CustomUserDetails) auth.getPrincipal()).getId();
        }
        Page<ReplyDTO> replies = commentService.getCommentReplies(commentId, userId, pageable.getPageNumber());
        return ResponseEntity.ok(replies);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNoSuchElementException(NoSuchElementException ex) {
        return ResponseEntity.status(404).body(ex.getMessage());
    }
}