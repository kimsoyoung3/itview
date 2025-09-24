package com.example.itview_spring.Service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.itview_spring.Constant.Channel;
import com.example.itview_spring.Constant.ContentType;
import com.example.itview_spring.Constant.Genre;
import com.example.itview_spring.DTO.ContentDTO;
import com.example.itview_spring.Repository.ContentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HomeService {
    
    private final ContentRepository contentRepository;

    // 컨텐츠 타입 별 목록 조회 (메인 화면)
    public Page<ContentDTO> getContentsByContentType(ContentType contentType, int page) {
        Pageable pageable = PageRequest.of(page - 1, 10);

        return contentRepository.findByContentType(contentType, pageable);
    }

    // 장르 목록 조회
    public List<Genre> getGenres(ContentType contentType) {
        return contentRepository.findGenresByContentType(contentType);
    }

    // 채널 목록 조회
    public List<Channel> getChannels(ContentType contentType) {
        return contentRepository.findChannelsByContentType(contentType);
    }

    // 장르 별 컨텐츠 조회
    public Page<ContentDTO> getContentsByGenre(ContentType contentType, Genre genre, int page) {
        Pageable pageable = PageRequest.of(page - 1, 10);
        return contentRepository.findByContentTypeAndGenre(contentType, genre, pageable);
    }

    // 채널 별 컨텐츠 조회
    public Page<ContentDTO> getContentsByChannel(ContentType contentType, Channel channel, int page) {
        Pageable pageable = PageRequest.of(page - 1, 10);
        return contentRepository.findByContentTypeAndChannel(contentType, channel, pageable);
    }
}
