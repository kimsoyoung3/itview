package com.example.itview_spring.Service;

import com.example.itview_spring.DTO.VideoDTO;
import com.example.itview_spring.Entity.ContentEntity;
import com.example.itview_spring.Entity.VideoEntity;
import com.example.itview_spring.Repository.ContentRepository;
import com.example.itview_spring.Repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Transactional
public class VideoService {

    private final VideoRepository videoRepository;
    private final ContentRepository contentRepository;

    /**
     * 1. 콘텐츠 기준 전체 영상 목록 조회
     */
    @Transactional(readOnly = true)
    public List<VideoEntity> getVideosByContentId(Integer contentId) {
        return videoRepository.findByContent_Id(contentId);
    }

    /**
     * 2. 영상 단건 조회
     */
    @Transactional(readOnly = true)
    public VideoEntity getVideoById(Integer videoId) {
        return videoRepository.findById(videoId)
                .orElseThrow(() -> new NoSuchElementException("해당 영상이 존재하지 않습니다. ID: " + videoId));
    }

    /**
     * 3. 유튜브 URL과 제목을 받아 영상 정보 생성 및 저장
     */
    public VideoDTO createVideo(Integer contentId, VideoDTO dto) {
        ContentEntity contentEntity = contentRepository.findById(contentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 콘텐츠가 존재하지 않습니다. ID: " + contentId));

        String url = dto.getUrl();
        String videoId = extractVideoId(url);
        if (videoId == null || videoId.isEmpty()) {
            throw new IllegalArgumentException("유효한 유튜브 URL이 아닙니다.");
        }

        String thumbnailUrl = createThumbnailUrl(videoId);
        String embedUrl = createEmbedUrl(videoId);

        VideoEntity entity = new VideoEntity();
        entity.setContent(contentEntity);
        // 사용자가 직접 입력한 제목을 저장
        entity.setTitle(dto.getTitle());
        entity.setImage(thumbnailUrl);
        entity.setUrl(embedUrl);

        VideoEntity saved = videoRepository.save(entity);
        return toDTO(saved);
    }

    /**
     * 4. 영상 정보 수정
     */
    public VideoDTO updateVideo(Integer id, VideoDTO dto) {
        VideoEntity existing = videoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 비디오가 존재하지 않습니다. ID: " + id));

        // URL이 변경되었을 경우 썸네일과 URL을 다시 생성
        if (!existing.getUrl().equals(dto.getUrl())) {
            String newVideoId = extractVideoId(dto.getUrl());
            if (newVideoId == null || newVideoId.isEmpty()) {
                throw new IllegalArgumentException("유효한 유튜브 URL이 아닙니다.");
            }
            existing.setImage(createThumbnailUrl(newVideoId));
            existing.setUrl(createEmbedUrl(newVideoId));
        }

        // 제목은 사용자가 입력한 값으로 업데이트
        existing.setTitle(dto.getTitle());

        VideoEntity updated = videoRepository.save(existing);
        return toDTO(updated);
    }

    /**
     * 5. 영상 삭제
     */
    public void deleteVideo(Integer videoId) {
        VideoEntity videoEntity = videoRepository.findById(videoId)
                .orElseThrow(() -> new NoSuchElementException("삭제할 비디오를 찾을 수 없습니다. ID: " + videoId));
        videoRepository.delete(videoEntity);
    }

    /**
     * 6. 유튜브 URL에서 영상 ID 추출 (내부 로직)
     */
    private String extractVideoId(String youtubeUrl) {
        String videoId = null;
        String regex = "(?<=watch\\?v=|/videos/|embed\\/|youtu.be\\/|\\/v\\/|\\/e\\/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%2Fvideos%2F|embed%2F|youtu.be%2F|v%2F)[^#&?\\n]*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(youtubeUrl);
        if (matcher.find()) {
            videoId = matcher.group();
        }
        return videoId;
    }

    /**
     * 7. 영상 ID로 썸네일 URL 생성 (내부 로직)
     */
    private String createThumbnailUrl(String videoId) {
        return "https://img.youtube.com/vi/" + videoId + "/hqdefault.jpg";
    }

    /**
     * 8. 영상 ID로 임베드 URL 생성 (내부 로직)
     */
    private String createEmbedUrl(String videoId) {
        return "https://www.youtube.com/embed/" + videoId;
    }

    /**
     * 9. Entity -> DTO 변환 (내부 로직)
     */
    private VideoDTO toDTO(VideoEntity entity) {
        return new VideoDTO(entity.getId(), entity.getTitle(), entity.getImage(), entity.getUrl());
    }
}