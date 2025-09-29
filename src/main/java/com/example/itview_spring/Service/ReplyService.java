package com.example.itview_spring.Service;

import java.util.NoSuchElementException;

import com.example.itview_spring.DTO.AdminReplyDTO;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.itview_spring.Constant.NotiType;
import com.example.itview_spring.Constant.Replyable;
import com.example.itview_spring.Entity.NotificationEntity;
import com.example.itview_spring.Entity.ReplyEntity;
import com.example.itview_spring.Repository.LikeRepository;
import com.example.itview_spring.Repository.NotificationRepository;
import com.example.itview_spring.Repository.ReplyRepository;
import com.example.itview_spring.Repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final NotificationService notificationService;
    private final ModelMapper modelMapper;

    // 댓글 수정
    public Boolean modifyReply(Integer userId, Integer replyId, String newText) {
        if (!replyRepository.existsById(replyId)) {
            throw new NoSuchElementException("존재하지 않는 댓글입니다");
        }
        ReplyEntity reply = replyRepository.findById(replyId).get();
        if (!reply.getUser().getId().equals(userId)) {
            throw new SecurityException("권한이 없습니다");
        }
        reply.setText(newText);
        replyRepository.save(reply);
        return true;
    }

    // 댓글 삭제
    public Boolean deleteReply(Integer userId, Integer replyId) {
        if (!replyRepository.existsById(replyId)) {
            throw new NoSuchElementException("존재하지 않는 댓글입니다");
        }
        ReplyEntity reply = replyRepository.findById(replyId).get();
        if (!reply.getUser().getId().equals(userId)) {
            throw new SecurityException("권한이 없습니다");
        }
        // 댓글에 달린 좋아요 먼저 삭제
        likeRepository.deleteByTargetIdAndTargetType(replyId, Replyable.REPLY);
        // 댓글 삭제
        replyRepository.deleteById(replyId);
        return true;
    }

    // 댓글에 좋아요 등록
    public Boolean likeReply(Integer userId, Integer replyId) {
        if (!replyRepository.existsById(replyId)) {
            throw new NoSuchElementException("존재하지 않는 댓글입니다");
        }
        if (likeRepository.existsByUserIdAndTargetIdAndTargetType(userId, replyId, Replyable.REPLY)) {
            return true;
        }
        try {
            likeRepository.likeTarget(userId, replyId, Replyable.REPLY);

            // 알림 생성
            ReplyEntity reply = replyRepository.findById(replyId).orElseThrow(() -> new NoSuchElementException("존재하지 않는 댓글입니다"));
            if (!reply.getUser().getId().equals(userId)) { // 본인에게는 알림을 보내지 않음
                NotificationEntity notification = new NotificationEntity();
                notification.setUser(reply.getUser());
                notification.setActor(userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("존재하지 않는 유저입니다")));
                notification.setType(NotiType.LIKE);
                notification.setTargetType(Replyable.REPLY);
                notification.setTargetId(replyId);
                notificationRepository.save(notification);
                // 실시간 알림 전송
                notificationService.sendNotification(reply.getUser().getId());
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 댓글에 좋아요 취소
    public Boolean unlikeReply(Integer userId, Integer replyId) {
        if (!replyRepository.existsById(replyId)) {
            throw new NoSuchElementException("존재하지 않는 댓글입니다");
        }
        try {
            likeRepository.unlikeTarget(userId, replyId, Replyable.REPLY);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 관리자 페이지 - 댓글 목록
    public Page<AdminReplyDTO> list(int userId, Pageable pageable, Replyable targetType) {
        // 이제 targetType을 사용해 레포지토리를 호출합니다.
        Page<ReplyEntity> repliesPage = replyRepository.findByUserIdAndTargetType(userId, targetType, pageable);
        return repliesPage.map(reply -> modelMapper.map(reply, AdminReplyDTO.class));
    }

    // 관리자 페이지 - 댓글 삭제
    public void delete(int id) {
        replyRepository.deleteById(id);
    }
}