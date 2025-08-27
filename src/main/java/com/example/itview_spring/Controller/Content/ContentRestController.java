package com.example.itview_spring.Controller.Content;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.itview_spring.Config.CustomUserDetails;
import com.example.itview_spring.DTO.CommentDTO;
import com.example.itview_spring.DTO.ContentDetailDTO;
import com.example.itview_spring.DTO.CreditDTO;
import com.example.itview_spring.DTO.RatingRequestDTO;
import com.example.itview_spring.DTO.TextDTO;
import com.example.itview_spring.Service.CommentService;
import com.example.itview_spring.Service.ContentService;
import com.example.itview_spring.Service.CreditService;
import com.example.itview_spring.Service.RatingService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/content")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class ContentRestController {

    private final ContentService contentService;
    private final CreditService creditService;
    private final CommentService commentService;
    private final RatingService ratingService;

    // 컨텐츠 상세 정보 조회
    @GetMapping("/{id}")
    public ResponseEntity<ContentDetailDTO> getContentDetail(@PathVariable("id") Integer id) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        Integer userId = 0;
        if (auth.getPrincipal() != "anonymousUser") {
            userId = ((CustomUserDetails) auth.getPrincipal()).getId();
        }
        ContentDetailDTO contentDetail = contentService.getContentDetail(id, userId);
        return ResponseEntity.ok(contentDetail);
    }

    // 컨텐츠 출연진 및 제작진 정보 조회 (페이징)
    @GetMapping("/{id}/credit")
    public ResponseEntity<Page<CreditDTO>> getContentCredit(@PageableDefault(page=1) Pageable pageable, @PathVariable("id") Integer id) {
        return ResponseEntity.ok(creditService.getCreditByContentId(pageable, id));
    }

    // 컨텐츠 별점 등록
    @PostMapping("/{id}/rating")
    public ResponseEntity<Void> postContentRating(@PathVariable("id") Integer id,
                                                  @AuthenticationPrincipal CustomUserDetails userDetails,
                                                  @RequestBody RatingRequestDTO ratingRequest) {
        ratingService.rateContent(userDetails.getId(), id, ratingRequest.getScore());
        return ResponseEntity.ok().build();
    }

    // 컨텐츠 별점 삭제
    @DeleteMapping("/{id}/rating")
    public ResponseEntity<Void> deleteContentRating(@PathVariable("id") Integer id,
                                                    @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (ratingService.deleteRating(userDetails.getId(), id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    // 컨텐츠 코멘트 작성
    @PostMapping("/{id}/comment")
    public ResponseEntity<Void> postContentComment(@PathVariable("id") Integer id,
                                                   @AuthenticationPrincipal CustomUserDetails userDetails,
                                                   @RequestBody TextDTO textDTO) {
        commentService.addComment(userDetails.getId(), id, textDTO.getText());

        return ResponseEntity.ok().build();
    }

    // 컨텐츠 코멘트 조회
    @GetMapping("/{id}/comment")
    public ResponseEntity<CommentDTO> getContentComment(@PathVariable("id") Integer id,
                                                        @AuthenticationPrincipal CustomUserDetails userDetails) {
        CommentDTO commentDTO = commentService.getCommentDTO(userDetails.getId(), id);
        return ResponseEntity.ok(commentDTO);
    }

    // 컨텐츠 코멘트 수정
    @PutMapping("/{commentId}/comment")
    public ResponseEntity<Void> putContentComment(@PathVariable("commentId") Integer commentId,
                                                  @AuthenticationPrincipal CustomUserDetails userDetails,
                                                  @RequestBody TextDTO textDTO) {
        commentService.updateComment(commentId, textDTO.getText());
        return ResponseEntity.ok().build();
    }

    // 컨텐츠 코멘트 삭제
    @DeleteMapping("/{commentId}/comment")
    public ResponseEntity<Void> deleteContentComment(@PathVariable("commentId") Integer commentId,
                                                     @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (commentService.deleteComment(commentId)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    // 컨텐츠 코멘트 페이징 조회
    @GetMapping("/{id}/comments")
    public ResponseEntity<Page<CommentDTO>> getContentComments(@PathVariable("id") Integer id,
                                                               @RequestParam(value = "order", defaultValue = "new") String order,
                                                               @PageableDefault(page=1) Pageable pageable) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        Integer userId = 0;
        if (auth.getPrincipal() != "anonymousUser") {
            userId = ((CustomUserDetails) auth.getPrincipal()).getId();
        }
        Page<CommentDTO> comments = contentService.getCommentsByContentId(id, userId, order, pageable.getPageNumber());
        return ResponseEntity.ok(comments);
    }
}