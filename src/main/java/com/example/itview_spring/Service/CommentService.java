package com.example.itview_spring.Service;

import com.example.itview_spring.Constant.Replyable;
import com.example.itview_spring.DTO.CommentAndContentDTO;
import com.example.itview_spring.DTO.CommentDTO;
import com.example.itview_spring.DTO.ReplyDTO;
import com.example.itview_spring.Entity.CommentEntity;
import com.example.itview_spring.Entity.ReplyEntity;
import com.example.itview_spring.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {
    
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ContentRepository contentRepository;
    private final ReplyRepository replyRepository;
    private final LikeRepository likeRepository;

    // 코멘트 추가
    public void addComment(Integer userId, Integer contentId, String text) {
        if (!contentRepository.existsById(contentId)) {
            throw new NoSuchElementException("Invalid contentId: " + contentId);
        }
        CommentEntity comment = new CommentEntity();
        comment.setUser(userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found")));
        comment.setContent(contentRepository.findById(contentId).orElseThrow(() -> new RuntimeException("Content not found")));
        comment.setText(text);
        commentRepository.save(comment);
    }

    // 코멘트 조회
    public CommentDTO getCommentDTO(Integer userId, Integer contentId) {
        if (!contentRepository.existsById(contentId)) {
            throw new NoSuchElementException("Invalid contentId: " + contentId);
        }
        CommentDTO myComment = commentRepository.findCommentDTOByUserIdAndContentId(userId, contentId).orElse(null);
        return myComment;
    }

    // 코멘트 + 컨텐츠 정보 조회
    public CommentAndContentDTO getCommentAndContentDTO(Integer commentId, Integer userId) {
        CommentAndContentDTO commentAndContent = commentRepository.findCommentAndContentByCommentId(commentId, userId).orElseThrow(() -> new NoSuchElementException("존재하지 않는 코멘트입니다"));
        return commentAndContent;
    }
    
    // 코멘트 수정
    public void updateComment(Integer commentId, String newText) {
        CommentEntity comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 코멘트입니다"));
        comment.setText(newText);
        commentRepository.save(comment);
    }

    // 코멘트 삭제
    public boolean deleteComment(Integer commentId) {
        var commentOpt = commentRepository.findById(commentId).orElseThrow(() -> new NoSuchElementException("존재하지 않는 코멘트입니다"));
        commentRepository.delete(commentOpt);
        likeRepository.deleteByTargetIdAndTargetType(commentId, Replyable.COMMENT);
        replyRepository.deleteByTargetIdAndTargetType(commentId, Replyable.COMMENT);
        return true;
    }

    // 코멘트에 좋아요 등록
    public void likeComment(Integer userId, Integer commentId) {
        if (!commentRepository.existsById(commentId)) {
            throw new NoSuchElementException("존재하지 않는 코멘트입니다");
        }
        likeRepository.likeTarget(userId, commentId, Replyable.COMMENT);
    }

    // 코멘트에 좋아요 취소
    public void unlikeComment(Integer userId, Integer commentId) {
        if (!commentRepository.existsById(commentId)) {
            throw new NoSuchElementException("존재하지 않는 코멘트입니다");
        }
        likeRepository.unlikeTarget(userId, commentId, Replyable.COMMENT);
    }

    // 코멘트에 댓글 작성
    public ReplyDTO addReply(Integer userId, Integer commentId, String text) {
        if (!commentRepository.existsById(commentId)) {
            throw new NoSuchElementException("존재하지 않는 코멘트입니다");
        }
        ReplyEntity reply = new ReplyEntity();
        reply.setUser(userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found")));
        reply.setTargetId(commentId);
        reply.setTargetType(Replyable.COMMENT);
        reply.setText(text);
        ReplyEntity savedReply = replyRepository.save(reply);
        ReplyDTO newReply = replyRepository.findReplyDTOById(userId, savedReply.getId());
        if (newReply == null) {
            throw new RuntimeException("Failed to create reply");
        }
        return newReply;
    }

    // 코멘트의 댓글 페이징 조회
    public Page<ReplyDTO> getCommentReplies(Integer commentId, Integer userId, Integer page) {
        if (!commentRepository.existsById(commentId)) {
            throw new NoSuchElementException("존재하지 않는 코멘트입니다");
        }
        Pageable pageable = PageRequest.of(page - 1, 10);
        Page<ReplyDTO> replies = replyRepository.findRepliesByTargetId(userId, commentId, Replyable.COMMENT, pageable);
        return replies;
    }
}