package com.example.itview_spring.Service;

import java.util.NoSuchElementException;

import com.example.itview_spring.DTO.AdminReplyDTO;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final ModelMapper modelMapper;

    // 댓글 수정
    public Boolean modifyReply(Integer replyId, String newText) {
        if (!replyRepository.existsById(replyId)) {
            throw new NoSuchElementException("존재하지 않는 댓글입니다");
        }
        ReplyEntity reply = replyRepository.findById(replyId).get();
        reply.setText(newText);
        replyRepository.save(reply);
        return true;
    }

    // 댓글 삭제
    public Boolean deleteReply(Integer replyId) {
        if (!replyRepository.existsById(replyId)) {
            throw new NoSuchElementException("존재하지 않는 댓글입니다");
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
    public Page<AdminReplyDTO> list(int userId, Pageable pageable) {
        Page<ReplyEntity> repliesPage = replyRepository.findByUserId(userId, pageable);
        return repliesPage.map(reply -> modelMapper.map(reply, AdminReplyDTO.class));
    }
}