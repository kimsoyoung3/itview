package com.example.itview_spring.Service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.itview_spring.Constant.Replyable;
import com.example.itview_spring.Entity.ReplyEntity;
import com.example.itview_spring.Repository.LikeRepository;
import com.example.itview_spring.Repository.ReplyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final LikeRepository likeRepository;

    // 댓글 수정
    public Boolean modifyReply(Integer replyId, String newText) {
        try {
            ReplyEntity reply = replyRepository.findById(replyId).orElseThrow(() -> new IllegalArgumentException("Reply not found"));
            reply.setText(newText);
            replyRepository.save(reply);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 댓글 삭제
    public Boolean deleteReply(Integer replyId) {
        try {
            // 댓글에 달린 좋아요 먼저 삭제
            likeRepository.deleteByTargetIdAndTargetType(replyId, Replyable.REPLY);
            // 댓글 삭제
            replyRepository.deleteById(replyId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 댓글에 좋아요 등록
    public Boolean likeReply(Integer userId, Integer replyId) {
        try {
            likeRepository.likeTarget(userId, replyId, Replyable.REPLY);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 댓글에 좋아요 취소
    public Boolean unlikeReply(Integer userId, Integer replyId) {
        try {
            likeRepository.unlikeTarget(userId, replyId, Replyable.REPLY);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}