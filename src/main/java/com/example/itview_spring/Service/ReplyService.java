package com.example.itview_spring.Service;

import org.springframework.stereotype.Service;

import com.example.itview_spring.Repository.ReplyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReplyService {

    private final ReplyRepository replyRepository;

    // 댓글에 좋아요 등록

}