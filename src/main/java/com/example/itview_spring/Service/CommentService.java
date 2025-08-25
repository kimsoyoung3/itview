package com.example.itview_spring.Service;

import org.springframework.stereotype.Service;

import com.example.itview_spring.Entity.CommentEntity;
import com.example.itview_spring.Repository.CommentRepository;
import com.example.itview_spring.Repository.ContentRepository;
import com.example.itview_spring.Repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {
    
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ContentRepository contentRepository;

    public void addComment(Integer userId, Integer contentId, String text) {
        CommentEntity comment = new CommentEntity();
        comment.setUser(userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found")));
        comment.setContent(contentRepository.findById(contentId).orElseThrow(() -> new RuntimeException("Content not found")));
        comment.setText(text);
        commentRepository.save(comment);
    }

    public void updateComment(Integer userId, Integer contentId, String newText) {
        CommentEntity comment = commentRepository.findByUserIdAndContentId(userId, contentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        comment.setText(newText);
        commentRepository.save(comment);
    }
}
