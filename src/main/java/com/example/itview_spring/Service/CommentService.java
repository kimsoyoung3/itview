package com.example.itview_spring.Service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.itview_spring.Constant.Replyable;
import com.example.itview_spring.DTO.CommentAndContentDTO;
import com.example.itview_spring.DTO.CommentDTO;
import com.example.itview_spring.Entity.CommentEntity;
import com.example.itview_spring.Repository.CommentRepository;
import com.example.itview_spring.Repository.ContentRepository;
import com.example.itview_spring.Repository.LikeRepository;
import com.example.itview_spring.Repository.ReplyRepository;
import com.example.itview_spring.Repository.UserRepository;

import lombok.RequiredArgsConstructor;

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
        CommentEntity comment = new CommentEntity();
        comment.setUser(userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found")));
        comment.setContent(contentRepository.findById(contentId).orElseThrow(() -> new RuntimeException("Content not found")));
        comment.setText(text);
        commentRepository.save(comment);
    }

    // 코멘트 조회
    public CommentDTO getCommentDTO(Integer userId, Integer contentId) {
        CommentDTO myComment = commentRepository.findCommentDTOByUserIdAndContentId(userId, contentId).orElse(null);
        return myComment;
    }

    // 코멘트 + 컨텐츠 정보 조회
    public CommentAndContentDTO getCommentAndContentDTO(Integer commentId, Integer userId) {
        CommentAndContentDTO commentAndContent = commentRepository.findCommentAndContentByCommentId(commentId, userId).orElse(null);
        return commentAndContent;
    }
    
    // 코멘트 수정
    public void updateComment(Integer commentId, String newText) {
        CommentEntity comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        comment.setText(newText);
        commentRepository.save(comment);
    }

    // 코멘트 삭제
    public boolean deleteComment(Integer commentId) {
        var commentOpt = commentRepository.findById(commentId);
        if (commentOpt.isPresent()) {
            commentRepository.delete(commentOpt.get());
            likeRepository.deleteByTargetIdAndTargetType(commentId, Replyable.COMMENT);
            replyRepository.deleteByTargetIdAndTargetType(commentId, Replyable.COMMENT);
            return true;
        }
        return false;
    }
}
