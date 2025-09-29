package com.example.itview_spring.Service;

import com.example.itview_spring.Constant.NotiType;
import com.example.itview_spring.Constant.Replyable;
import com.example.itview_spring.DTO.AdminCommentDTO;
import com.example.itview_spring.DTO.CommentAndContentDTO;
import com.example.itview_spring.DTO.CommentDTO;
import com.example.itview_spring.DTO.ReplyDTO;
import com.example.itview_spring.Entity.CommentEntity;
import com.example.itview_spring.Entity.NotificationEntity;
import com.example.itview_spring.Entity.ReplyEntity;
import com.example.itview_spring.Repository.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {
    
    private final CommentRepository commentRepository;
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final ContentRepository contentRepository;
    private final ReplyRepository replyRepository;
    private final LikeRepository likeRepository;
    private final NotificationService notificationService;
    private final ModelMapper modelMapper;

    // 코멘트 추가
    public void addComment(Integer userId, Integer contentId, String text) {
        if (!contentRepository.existsById(contentId)) {
            throw new NoSuchElementException("존재하지 않는 컨텐츠입니다");
        }
        if (commentRepository.findByUserIdAndContentId(userId, contentId).isPresent()) {
            throw new IllegalStateException("이미 코멘트를 작성한 컨텐츠입니다");
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
            throw new NoSuchElementException("존재하지 않는 컨텐츠입니다");
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
    public void updateComment(Integer userId, Integer commentId, String newText) {
        if (!commentRepository.existsById(commentId)) {
            throw new NoSuchElementException("존재하지 않는 코멘트입니다");
        }
        CommentEntity comment = commentRepository.findById(commentId).get();
        if (!comment.getUser().getId().equals(userId)) {
            throw new SecurityException("권한이 없습니다");
        }
        comment.setText(newText);
        commentRepository.save(comment);
    }

    // 코멘트 삭제
    public boolean deleteComment(Integer userId, Integer commentId) {
        if (!commentRepository.existsById(commentId)) {
            throw new NoSuchElementException("존재하지 않는 코멘트입니다");
        }
        CommentEntity comment = commentRepository.findById(commentId).get();
        if (!comment.getUser().getId().equals(userId)) {
            throw new SecurityException("권한이 없습니다");
        }
        commentRepository.delete(comment);
        likeRepository.deleteByTargetIdAndTargetType(commentId, Replyable.COMMENT);
        replyRepository.deleteByTargetIdAndTargetType(commentId, Replyable.COMMENT);
        return true;
    }

    // 코멘트에 좋아요 등록
    public void likeComment(Integer userId, Integer commentId) {
        if (!commentRepository.existsById(commentId)) {
            throw new NoSuchElementException("존재하지 않는 코멘트입니다");
        }
        if (likeRepository.existsByUserIdAndTargetIdAndTargetType(userId, commentId, Replyable.COMMENT)) {
            return;
        }
        likeRepository.likeTarget(userId, commentId, Replyable.COMMENT);

        // 알림 생성
        CommentEntity comment = commentRepository.findById(commentId).orElseThrow(() -> new NoSuchElementException("존재하지 않는 코멘트입니다"));
        if (!comment.getUser().getId().equals(userId)) { // 본인에게는 알림을 보내지 않음
            NotificationEntity notification = new NotificationEntity();
            notification.setUser(comment.getUser());
            notification.setActor(userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("존재하지 않는 유저입니다")));
            notification.setType(NotiType.LIKE);
            notification.setTargetType(Replyable.COMMENT);
            notification.setTargetId(commentId);
            notificationRepository.save(notification);
            // 실시간 알림 전송
            notificationService.sendNotification(comment.getUser().getId());
        }
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
        reply.setUser(userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("존재하지 않는 유저입니다")));
        reply.setTargetId(commentId);
        reply.setTargetType(Replyable.COMMENT);
        reply.setText(text);
        ReplyEntity savedReply = replyRepository.save(reply);
        ReplyDTO newReply = replyRepository.findReplyDTOById(userId, savedReply.getId());
        if (newReply == null) {
            throw new RuntimeException("Failed to create reply");
        }
        
        // 알림 생성

        // 해당 코멘트 작성자에게 알림 전송
        CommentEntity comment = commentRepository.findById(commentId).orElseThrow(() -> new NoSuchElementException("존재하지 않는 코멘트입니다"));
        if (!comment.getUser().getId().equals(userId)) { // 본인에게는 알림을 보내지 않음
            NotificationEntity notification = new NotificationEntity();
            notification.setUser(comment.getUser());
            notification.setActor(userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("존재하지 않는 유저입니다")));
            notification.setType(NotiType.REPLY);
            notification.setTargetType(Replyable.COMMENT);
            notification.setTargetId(commentId);
            notificationRepository.save(notification);
            // 실시간 알림 전송
            notificationService.sendNotification(comment.getUser().getId());
        }

        // 해당 코멘트에 댓글을 단 모든 유저에게 알림 전송
        List<Integer> recipientIds = commentRepository.findAllReplyUserIdsByCommentId(commentId);
        for (Integer recipientId : recipientIds) {
            NotificationEntity notification = new NotificationEntity();
            if (!recipientId.equals(userId)) { // 본인에게는 알림을 보내지 않음
                notification.setUser(userRepository.findById(recipientId).orElseThrow(() -> new NoSuchElementException("존재하지 않는 유저입니다")));
                notification.setActor(userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("존재하지 않는 유저입니다")));
                notification.setType(NotiType.REPLY);
                notification.setTargetType(Replyable.COMMENT);
                notification.setTargetId(commentId);
                notificationRepository.save(notification);
                // 실시간 알림 전송
                notificationService.sendNotification(recipientId);
            }
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

    // 관리자 페이지 - 코멘트 조회
    public Page<AdminCommentDTO> list(int userId, Pageable pageable) {
        // 1. 레포지토리를 통해 특정 사용자의 코멘트 엔티티 목록을 조회합니다.
        Page<CommentEntity> commentsPage = commentRepository.findByUserId(userId, pageable);

        // 2. 조회된 엔티티 페이지를 AdminCommentDTO 페이지로 변환하여 반환합니다.
        return commentsPage.map(comment -> modelMapper.map(comment, AdminCommentDTO.class));
    }

    // 관리자 페이지 - 코멘트 삭제
    public void delete(int id) {
        commentRepository.deleteById(id);
    }
}