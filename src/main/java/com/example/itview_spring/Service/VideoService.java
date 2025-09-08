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

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional
public class VideoService {

    private final VideoRepository videoRepository;
    private final ContentRepository contentRepository;
    private final ModelMapper modelMapper;

    // 1. 전체 조회 (Get all videos contentId 기준 모든영상)
    // ✔️ 목록용: 여러 영상 조회 (DTO 리스트)
    @Transactional(readOnly = true)
    public List<VideoDTO> getVideosByContentId(Integer contentId) {
        // 변경: Repository에서 DTO를 바로 반환하도록 수정
        return videoRepository.findByContentId(contentId); // Repository에서 DTO 바로 반환
    }
//    VideoRepository에 이미 JPQL로 List<VideoDTO> findByContentId(Integer contentId)를 정의해두셨으므로,
//    return videoRepository.findByContentId(contentId); //처럼 한 줄로 바로 반환할 수도 있습니다.

    // 2. 단일 VideoDTO 조회 (수정 모드)
    @Transactional(readOnly = true)
    public VideoDTO getVideoById(Integer videoId) {
        // 변경 없음: 여전히 VideoEntity -> VideoDTO 변환
        VideoEntity videoEntity = videoRepository.findById(videoId)
                .orElseThrow(() -> new NoSuchElementException("해당 영상이 존재하지 않습니다: " + videoId));

        // 명시적 매핑 규칙 추가
        modelMapper.addMappings(new PropertyMap<VideoEntity, VideoDTO>() {
            protected void configure() {
                map(source.getTitle(), destination.getTitle()); // 제목 매핑
                map(source.getImage(), destination.getImage()); // 이미지 매핑
                map(source.getUrl(), destination.getUrl());     // URL 매핑
            }
        });
        // 명시적인 매핑 규칙이 적용된 ModelMapper 사용
        return modelMapper.map(videoEntity, VideoDTO.class);
    }
//
//    // 첫 번째 영상 조회: contentId 기준
//    @Transactional(readOnly = true)
//    public VideoDTO getFirstVideoByContentId(Integer contentId) {
//        Optional<VideoEntity> videoOpt = videoRepository.findFirstByContentId(contentId);
//        if (videoOpt.isPresent()) {
//            VideoEntity v = videoOpt.get();
//            // 명시적 DTO 생성자 사용 → JPQL Projection 호환
//            return new VideoDTO(v.getId(), v.getTitle(), v.getImage(), v.getUrl());
//        }
//        return null;
//    }
//
//
    // 3. 입력 (Create new video)
    @Transactional
    public VideoDTO createVideo(Integer contentId, VideoDTO videoDTO) {
        VideoEntity entity = new VideoEntity();
        entity.setTitle(videoDTO.getTitle());
        entity.setImage(videoDTO.getImage());
        entity.setUrl(videoDTO.getUrl());

        // contentId를 이용하여 ContentEntity를 찾아서 VideoEntity와 연결
        ContentEntity contentEntity = contentRepository.findById(contentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 콘텐츠가 존재하지 않습니다. ID: " + contentId));
        entity.setContent(contentEntity);

        videoRepository.save(entity);

        return new VideoDTO(entity.getId(), entity.getTitle(), entity.getImage(), entity.getUrl());
    }

    // 4. 수정 (Update existing video)
    @Transactional
    public VideoDTO updateVideo(Integer id, VideoDTO dto) {
        VideoEntity entity = videoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 영상 서비스가 존재하지 않습니다. ID: " + id));

        entity.setTitle(dto.getTitle());
        entity.setImage(dto.getImage());
        entity.setUrl(dto.getUrl());
        videoRepository.save(entity);

        return new VideoDTO(entity.getId(), entity.getTitle(), entity.getImage(), entity.getUrl());
    }

    // 5. 삭제 (Delete video)
    @Transactional
    public void deleteVideo(Integer videoId) {
        // 주어진 videoId로  엔티티 조회
        VideoEntity videoEntity = videoRepository.findById(videoId)
                .orElseThrow(() -> new NoSuchElementException("삭제할 영상 서비스를 찾을 수 없습니다. ID: " + videoId));

        // videoId로 엔티티 삭제
        videoRepository.delete(videoEntity);
    }

}