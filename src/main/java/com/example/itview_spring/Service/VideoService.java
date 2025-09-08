package com.example.itview_spring.Service;


import com.example.itview_spring.DTO.*;
import com.example.itview_spring.Entity.ContentEntity;
import com.example.itview_spring.Entity.VideoEntity;
import com.example.itview_spring.Repository.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class VideoService {
    private final VideoRepository videoRepository;
    private final ContentRepository contentRepository;
    private final ModelMapper modelMapper;

    private final GalleryRepository galleryRepository;
    private final ExternalServiceRepository externalServiceRepository;
    private final RatingRepository ratingRepository;
    private final UserRepository userRepository;

    //전체조회
    //목록(전체조회)
    //모두 읽어서 list<방금 작성한 DTO> 전달
    //public 전달할 모양 이름은 마음대로(받을것{
    //
    //     사용한 Repository.작업할 함수.var
    //     return 전달할 값;
    //}
    // public List<ProductDTO> 안알려줌() {
    // public List<ProductDTO> List() {      ex)
    /**
     * 전체 목록조회
     *
     * @param page 조회할 페이지 번호
     * @return 결과
     */

    public Page<VideoDTO> getAllVideos(Pageable page) {
        int currentPage = page.getPageNumber()-1;
        int pageLimits = 10 ;

        Pageable pageable = PageRequest.of(currentPage,pageLimits, Sort.by(Sort.Direction.DESC, "id"));
        Page<VideoEntity> videoEntities = videoRepository.findAll(pageable) ;
        Page<VideoDTO> videoDTOS = videoEntities.map(data->modelMapper.map(
                data, VideoDTO.class));
        return videoDTOS;
    }
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
    public List<VideoDTO> getVideosByContentId(Integer contentId) {
        List<VideoEntity> videos = videoRepository.findByContent_Id(contentId);
        List<VideoDTO> videoDTOS = Arrays.asList(modelMapper.map(videos, VideoDTO[].class));
        return videoDTOS;
    }
//    VideoRepository에 이미 JPQL로 List<VideoDTO> findByContentId(Integer contentId)를 정의해두셨으므로,
//    return videoRepository.findByContentId(contentId); //처럼 한 줄로 바로 반환할 수도 있습니다.

    // 2. 개별 조회 (Get videoId기준 )
    // ✔️ 단일용: 콘텐츠에 연결된 첫 번째 영상 조회
    @Transactional(readOnly = true)
    public VideoDTO getVideoByContentId(Integer contentId) {
        Optional<VideoEntity> videoOpt = videoRepository.findFirstByContentId(contentId);
        if (videoOpt.isPresent()) {
            VideoEntity v = videoOpt.get();
            // 명시적 DTO 생성자 사용 → JPQL Projection 호환
            return new VideoDTO(v.getId(), v.getTitle(), v.getImage(), v.getUrl());
        }
        return null;
    }

    // 3. 입력 (Create new video)
    public VideoDTO createVideo(Integer contentId, VideoDTO dto) {
        // ✅ URL에서 넘어온 contentId 활용
        ContentEntity contentEntity = contentRepository.findById(contentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 콘텐츠가 존재하지 않습니다. ID: " + contentId));

        VideoEntity entity = new VideoEntity();
        entity.setTitle(dto.getTitle());
        entity.setImage(dto.getImage());
        entity.setUrl(dto.getUrl());
        entity.setContent(contentEntity);
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