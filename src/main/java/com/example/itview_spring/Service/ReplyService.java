package com.example.itview_spring.Service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.itview_spring.Constant.Replyable;
import com.example.itview_spring.Repository.LikeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ReplyService {

    private final LikeRepository likeRepository;

    // 댓글에 좋아요 등록
    public Boolean likeReply(Integer userId, Integer replyId) {
        likeRepository.likeTarget(userId, replyId, Replyable.REPLY);
        return true;
    }

    // 댓글에 좋아요 취소
    public Boolean unlikeReply(Integer userId, Integer replyId) {
        likeRepository.unlikeTarget(userId, replyId, Replyable.REPLY);
        return true;
    }
}