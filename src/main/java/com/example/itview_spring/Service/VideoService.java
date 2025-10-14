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

        String url = dto.getUrl(); // 👈 원본 URL
        String videoId = extractVideoId(url);

        // (이 부분은 안정성을 위해 유지합니다)
        if (videoId == null || videoId.isEmpty() || videoId.length() != 11) {
            throw new IllegalArgumentException("유효한 유튜브 URL이 아닙니다.");
        }

        String thumbnailUrl = createThumbnailUrl(videoId);
        // String embedUrl = createEmbedUrl(videoId); // 👈 이 줄 삭제

        VideoEntity entity = new VideoEntity();
        entity.setContent(contentEntity);
        // 사용자가 직접 입력한 제목을 저장
        entity.setTitle(dto.getTitle());
        entity.setImage(thumbnailUrl);
        entity.setUrl(url); // 👈 **(수정)** 원본 URL을 그대로 저장

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
            if (newVideoId == null || newVideoId.isEmpty() || newVideoId.length() != 11) {
                throw new IllegalArgumentException("유효한 유튜브 URL이 아닙니다.");
            }
            existing.setImage(createThumbnailUrl(newVideoId));
            existing.setUrl(dto.getUrl()); // 👈 **(수정)** 원본 URL을 그대로 저장
            // existing.setUrl(createEmbedUrl(newVideoId)); // 👈 이 줄 삭제
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

        // 정규식: watch?v=, youtu.be/, shorts/, embed/, live/ 등 모든 주요 형식 처리
        // 11자리 영숫자, 하이픈, 언더바 문자를 캡처합니다.
        String regex = "(?:youtube\\.com\\/(?:[^\\/]+\\/.+\\/|(?:v|e(?:mbed)?)\\/|.*[?&]v=)|youtu\\.be\\/|shorts\\/|live\\/)([^\"&?\\/\\s]{11})";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(youtubeUrl);

        if (matcher.find()) {
            // 정규식의 그룹 1 (정확히 11자리 VIDEO_ID) 반환
            videoId = matcher.group(1);
        }

        // ID가 추출되었더라도, 길이가 11이 아니면 유효하지 않은 것으로 간주
        if (videoId != null && videoId.length() == 11) {
            return videoId;
        }
        return null; // ID가 없거나 길이가 11이 아니면 null 반환
    }

    /**
     * 7. 영상 ID로 썸네일 URL 생성 (내부 로직)
     */
    private String createThumbnailUrl(String videoId) {
        return "https://img.youtube.com/vi/" + videoId + "/hqdefault.jpg";
    }


    /**
     * 9. Entity -> DTO 변환 (내부 로직)
     */
    private VideoDTO toDTO(VideoEntity entity) {
        return new VideoDTO(entity.getId(), entity.getTitle(), entity.getImage(), entity.getUrl());
    }
}