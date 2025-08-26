package com.example.itview_spring.Service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public void addComment(Integer userId, Integer contentId, String text) {
        CommentEntity comment = new CommentEntity();
        comment.setUser(userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found")));
        comment.setContent(contentRepository.findById(contentId).orElseThrow(() -> new RuntimeException("Content not found")));
        comment.setText(text);
        commentRepository.save(comment);
    }

    public CommentDTO getCommentDTO(Integer userId, Integer contentId) {
        CommentDTO myComment = commentRepository.findCommentDTOByUserIdAndContentId(userId, contentId).orElse(null);
        return myComment;
    }

    public void updateComment(Integer commentId, String newText) {
        CommentEntity comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        comment.setText(newText);
        commentRepository.save(comment);
    }

    public boolean deleteComment(Integer commentId) {
        var commentOpt = commentRepository.findById(commentId);
        if (commentOpt.isPresent()) {
            commentRepository.delete(commentOpt.get());
            likeRepository.deleteByTargetIdAndTargetType(commentId, "COMMENT");
            replyRepository.deleteByTargetIdAndTargetType(commentId, "COMMENT");
            return true;
        }
        return false;
    }
}
