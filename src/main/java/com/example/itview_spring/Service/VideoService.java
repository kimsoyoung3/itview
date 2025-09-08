package com.example.itview_spring.Service;


import com.example.itview_spring.DTO.*;
import com.example.itview_spring.Entity.ContentEntity;
import com.example.itview_spring.Entity.VideoEntity;
import com.example.itview_spring.Repository.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class VideoService {

    private final VideoRepository videoRepository;
    private final ContentRepository contentRepository;
    private final ModelMapper modelMapper;

//    public List<VideoDTO> List() {
//        //읽기,수정/저장/삭제 ==>Repository
//        List<VideoEntity> videoEntities = videoRepository.findAll();
//        //Entity =있으면 밑에 DTO변환
//        List<VideoDTO> videoDTOs = Arrays.asList(modelMapper.map(videoEntities, VideoDTO[].class));
//        //DTO가 보이면 return DTO를지정
//        return videoDTOs;

    /// ///////////////////////////////////////////////////////////////////////////////////////////
    /// 0825 video service 작성함
    /// ///////////////////////////////////////////////////////////////////////////////////////////
    // 1. 전체 조회 (Get all videos contentId 기준 모든영상)
    // ✔️ 목록용: 여러 영상 조회 (DTO 리스트)
    @Transactional(readOnly = true)
    public List<VideoEntity> getVideosByContentId(Integer contentId) {
        return videoRepository.findByContent_Id(contentId);
    }

    // 2. 개별 조회 (Get videoId기준 )
    @Transactional(readOnly = true)
    public VideoEntity getVideoById(Integer videoId) {
        return videoRepository.findById(videoId)
                .orElseThrow(() -> new NoSuchElementException("해당 영상이 존재하지 않습니다. ID: " + videoId));
    }


    // 3. 입력 (Create new video)
    public VideoDTO createVideo(Integer contentId, VideoDTO dto) {
        // ✅ URL에서 넘어온 contentId 활용
        ContentEntity contentEntity = contentRepository.findById(contentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 콘텐츠가 존재하지 않습니다. ID: " + contentId));

        VideoEntity entity = new VideoEntity();
        entity.setContent(contentEntity);
        entity.setTitle(dto.getTitle());
        entity.setImage(dto.getImage());
        entity.setUrl(dto.getUrl());

        System.out.println(" createVideo contentId :"+contentId);

        VideoEntity saved = videoRepository.save(entity);
        return new VideoDTO(saved.getId(), saved.getTitle(), saved.getImage(), saved.getUrl());
    }

    // 4. 수정 (Update existing video)
    public VideoDTO updateVideo(Integer id, VideoDTO dto) {
        VideoEntity existing = videoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 비디오가 존재하지 않습니다. ID: " + id));

        existing.setTitle(dto.getTitle());
        existing.setUrl(dto.getUrl());
        existing.setImage(dto.getImage());

        VideoEntity updated = videoRepository.save(existing);
        return new VideoDTO(updated.getId(), updated.getTitle(), updated.getImage(), updated.getUrl());
    }
    // 5. 삭제 (Delete video)
    public void deleteVideo(Integer videoId) {
        // 주어진 videoId로 비디오 엔티티 조회
        VideoEntity videoEntity = videoRepository.findById(videoId)
                .orElseThrow(() -> new NoSuchElementException("삭제할 비디오를 찾을 수 없습니다. ID: " + videoId));

        // 비디오 엔티티 삭제
        videoRepository.delete(videoEntity);
    }
}