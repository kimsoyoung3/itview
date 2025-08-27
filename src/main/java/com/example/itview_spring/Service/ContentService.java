package com.example.itview_spring.Service;

import com.example.itview_spring.DTO.CommentDTO;
import com.example.itview_spring.DTO.ContentCreateDTO;
import com.example.itview_spring.DTO.ContentDetailDTO;
import com.example.itview_spring.DTO.ContentResponseDTO;
import com.example.itview_spring.DTO.ExternalServiceDTO;
import com.example.itview_spring.DTO.GenreDTO;
import com.example.itview_spring.DTO.ImageDTO;
import com.example.itview_spring.DTO.RatingCountDTO;
import com.example.itview_spring.DTO.UserProfileDTO;
import com.example.itview_spring.DTO.VideoDTO;

import com.example.itview_spring.Constant.Genre;
import com.example.itview_spring.DTO.ContentCreateDTO;
import com.example.itview_spring.Entity.ContentEntity;
import com.example.itview_spring.Entity.VideoEntity;
import com.example.itview_spring.Repository.CommentRepository;
import com.example.itview_spring.Repository.ContentGenreRepository;
import com.example.itview_spring.Entity.ContentGenreEntity;
import com.example.itview_spring.Entity.RatingEntity;
import com.example.itview_spring.Repository.ContentGenreRepository;
import com.example.itview_spring.Repository.ContentRepository;
import com.example.itview_spring.Repository.ExternalServiceRepository;
import com.example.itview_spring.Repository.GalleryRepository;
import com.example.itview_spring.Repository.RatingRepository;
import com.example.itview_spring.Repository.UserRepository;
import com.example.itview_spring.Repository.VideoRepository;

import io.swagger.v3.oas.annotations.media.Content;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional
public class ContentService {

    private final CommentService commentService;

    private final ContentRepository contentRepository;
    private final ContentGenreRepository contentGenreRepository;
    private final GalleryRepository galleryRepository;
    private final VideoRepository videoRepository;
    private final ExternalServiceRepository externalServiceRepository;
    private final RatingRepository ratingRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

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

    public Page<ContentCreateDTO> getAllContents(Pageable page) {
        int currentPage = page.getPageNumber() - 1;
        int pageLimits = 10;

        Pageable pageable = PageRequest.of(currentPage, pageLimits, Sort.by(Sort.Direction.DESC, "id"));
        Page<ContentEntity> contentEntities = contentRepository.findAll(pageable);
        Page<ContentCreateDTO> contentDTOS = contentEntities.map(data -> modelMapper.map(
                data, ContentCreateDTO.class));
        return contentDTOS;
    }
//    public List<ContentCreateDTO> List() {
//        //읽기,수정/저장/삭제 ==>Repository
//        List<ContentEntity> contentEntities = contentRepository.findAll();
//        //Entity =있으면 밑에 DTO변환
//        List<ContentCreateDTO> contentDTOs = Arrays.asList(modelMapper.map(contentEntities, ContentCreateDTO[].class));
//        //DTO가 보이면 return DTO를지정
//        return contentDTOs;
//    }



    //상세보기,수정(개별조회)
    //주문번호를 받아서 해당하는 DTO에 전달
    //public ProductDTO 역시 안알려줌(Integer id) {
    //public ProductDTO read(Integer id) {    ex)
    public ContentCreateDTO read(Integer id) {
        //해당내용을 조회
        if (id == null) {
            throw new IllegalArgumentException("id는 null일 수 없습니다.");
        }
        Optional<ContentEntity> contentEntity = contentRepository.findById(id);
        if (contentEntity.isEmpty()) {
            throw new NoSuchElementException("해당 ID에 대한 콘텐츠를 찾을 수 없습니다: " + id);
        }
        ContentCreateDTO adminContentDTO = modelMapper.map(contentEntity.get(), ContentCreateDTO.class);
        return adminContentDTO;
    }

    //등록(저장)
    //DTO를 받아서 저장
    //public void 내맘대로 (ProductDTO productDTO) {
    //public void create (ProductDTO productDTO) {  ex)
    public ContentCreateDTO create(ContentCreateDTO dto) {
        //DTO가 이있으면 반드시 Entity 변환

        ContentEntity contentEntity = modelMapper.map(dto, ContentEntity.class);
        System.out.println("service add dto:" + dto);
        System.out.println("service add entity:" + contentEntity);

        contentRepository.save(contentEntity);
        return modelMapper.map(contentEntity, ContentCreateDTO.class);
    }

    //수정
    //주문번호와 DTO를 받아서, 주문번호로 조회해서 DTO의 내용을 저장
    // public void 수정할까(Integer orderId, ProductDTO productDTO) {
    // public void update(Integer orderId, ProductDTO productDTO) {   ex)
    public ContentCreateDTO update(Integer id, ContentCreateDTO dto) {
        //해당내용찾기
//        System.out.println("dto:"+dto);
        ContentEntity contentEntity = contentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("콘텐츠 ID가 유효하지 않습니다: " + id));

        if (contentEntity == null) {
            return null;
        }
//        System.out.println("entity:"+contentEntity);
//내용을 저장(@Id가 있는 변수는 저장 불가)
        contentEntity.setTitle(dto.getTitle());
        contentEntity.setContentType(dto.getContentType());
        contentEntity.setReleaseDate(dto.getReleaseDate());
        contentEntity.setPoster(dto.getPoster());
        contentEntity.setNation(dto.getNation());
        contentEntity.setDescription(dto.getDescription());
        contentEntity.setDuration(dto.getDuration());
        contentEntity.setAge(dto.getAge());
        contentEntity.setCreatorName(dto.getCreatorName());
        contentEntity.setChannelName(dto.getChannelName());

        contentRepository.save(contentEntity);
        return modelMapper.map(contentEntity, ContentCreateDTO.class);
    }

    //삭제
    //주문번호를 받아서 삭제
    //  public void 삭제가될까(Integer id) {
    //  public void delete(Integer id) {
//    public boolean delete(Integer id) {
//        // First delete related entries in content_genre_entity
//        if(contentRepository.existsById(id)) { //데이터가 존재하면
//            contentGenreRepository.deleteByContentId(id); // Assuming you have a method in repository for this
//
//            // Then delete the content entity
//            contentRepository.deleteById(id); //삭제
//            return true;
//        }
//        return false;
//    }


    @Transactional
    public void delete(Integer id) {
        ContentEntity content = contentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("콘텐츠 ID가 유효하지 않습니다. id: " + id));

        // Delete the related genres first
        contentGenreRepository.deleteByContent(content);

        // Now delete the content entity
        contentRepository.delete(content);
    }


    // 컨텐츠 상세 정보 조회
    public ContentDetailDTO getContentDetail(Integer contentId, Integer userId) {
        try {
            ContentDetailDTO contentDetail = new ContentDetailDTO();

            // 컨텐츠 정보 조회
            ContentResponseDTO contentResponseDTO = contentRepository.findContentWithAvgRating(contentId);
            // 컨텐츠 장르 조회
            List<GenreDTO> genres = contentGenreRepository.findByContentId(contentId);
            genres.forEach(genre -> {
                contentResponseDTO.getGenres().add(genre.getGenre().getGenreName());
            });
            contentDetail.setContentInfo(contentResponseDTO);

            // 갤러리 이미지 조회
            List<ImageDTO> images = galleryRepository.findByContentId(contentId);
            contentDetail.setGallery(images);

            // 동영상 조회
            List<VideoDTO> videos = videoRepository.findByContentId(contentId);
            contentDetail.setVideos(videos);

            // 외부 서비스 조회
            List<ExternalServiceDTO> externalServices = externalServiceRepository.findByContentId(contentId);
            contentDetail.setExternalServices(externalServices);

            // 사용자 별점 조회
            Integer myRating = ratingRepository.findSomeoneScore(userId, contentId);
            contentDetail.setMyRating(myRating != null ? myRating : 0);

            // 별점 개수 조회
            Long ratingCount = ratingRepository.countByContentId(contentId);
            contentDetail.setRatingCount(ratingCount != null ? ratingCount : 0L);

            // 별점 분포 조회
            List<RatingCountDTO> ratingDistribution = ratingRepository.findRatingDistributionByContentId(contentId);
            List<RatingCountDTO> fullRating = new ArrayList<>();
            Map<Integer, Long> ratingMap = ratingDistribution.stream()
                    .collect(Collectors.toMap(RatingCountDTO::getScore, RatingCountDTO::getScoreCount));
            for (int i = 1; i <= 10; i++) {
                Long count = ratingMap.getOrDefault(i, 0L);
                fullRating.add(new RatingCountDTO(i, count));
            }
            contentDetail.setRatingDistribution(fullRating);

            // 사용자 코멘트 조회
            CommentDTO myComment = commentService.getCommentDTO(userId, contentId);
            if (myComment != null) {
                contentDetail.setMyComment(myComment);
            }

            // 컨텐츠 좋아요 상위 8개 코멘트 조회
            List<CommentDTO> comments = commentRepository.findTop8CommentsByContentId(userId, contentId);
            contentDetail.setComments(comments);

            // 코멘트 개수 조회
            Long commentCount = commentRepository.countByContentId(contentId);
            contentDetail.setCommentCount(commentCount);

            return contentDetail;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    // 컨텐츠의 코멘트 페이징 조회
    public Page<CommentDTO> getCommentsByContentId(Integer contentId, Integer userId, String order, int page) {
        Pageable pageable = PageRequest.of(page - 1, 5);
        return commentRepository.findByContentId(userId, contentId, order, pageable);
    }

    //////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 콘텐츠에 장르 추가
     *///
    //장르조회
    @Transactional
    public List<Genre> getGenresByContentId(Integer contentId) {
        ContentEntity content = contentRepository.findById(contentId)
                .orElseThrow(() -> new NoSuchElementException("콘텐츠 ID가 유효하지 않습니다. id: " + contentId));

        List<ContentGenreEntity> genreEntities = contentGenreRepository.findByContent(content);

        return genreEntities.stream()
                .map(ContentGenreEntity::getGenre)
                .collect(Collectors.toList());
    }

    // @Transactional
    public void addGenres(Integer contentId, List<Genre> genres) {
        ContentEntity content = contentRepository.findById(contentId)
                .orElseThrow(() -> new IllegalArgumentException("콘텐츠 ID가 유효하지 않습니다. id: " + contentId));
        List<Genre> existingGenres = getGenresByContentId(contentId);
// 0825 주석함
//        for (Genre genre : genres) {
//            if (existingGenres.contains(genre)) {
//                continue; // 이미 있는 장르는 건너뜀
//            }
//            System.out.println(" add contentId :"+contentId);
//            System.out.println(" add content :"+content);
//            System.out.println(" add genre :"+genre);
//            System.out.println(" -----------------");
//
//            ContentGenreEntity contentGenre = new ContentGenreEntity();
//            contentGenre.setContent(content);// ✅ null 아님
//            contentGenre.setGenre(genre);  // ✅ 여기서 content가 null이면 에러 발생
//
//            contentGenreRepository.save(contentGenre);
     //   Set<Genre> existingGenreSet = new HashSet<>(existingGenres);
        for (Genre genre : genres) {
            if (!existingGenres.contains(genre)) {  // Avoid adding existing genre
                ContentGenreEntity contentGenre = new ContentGenreEntity();
                contentGenre.setContent(content);
                contentGenre.setGenre(genre);
                contentGenreRepository.save(contentGenre);
            }
        }
    }
//    @Transactional
//    public void saveContentGenres(Integer contentId, List<String> genreNames) {
//        Content content = contentRepository.findById(contentId)
//                .orElseThrow(() -> new IllegalArgumentException("콘텐츠를 찾을 수 없습니다."));
//
//        // 기존 장르 제거 (전체 삭제 후 재등록 방식)
//        contentGenreRepository.deleteByContent(content);
//
//        // 새 장르 등록
//        if (genreNames != null) {
//            for (String genreName : genreNames) {
//                Genre genre = Genre.valueOf(genreName); // 🔥 여기에 잘못된 값 들어오면 예외 발생!
//                ContentGenreEntity genreEntity = new ContentGenreEntity();
//                genreEntity.setContent(content);
//                genreEntity.setGenre(genre);
//                contentGenreRepository.save(genreEntity);
//            }
//        }
//    }
    /**
     * 콘텐츠 장르 수정 (기존 장르 모두 삭제 후, 새로 추가)
     */
    public void updateGenres(Integer contentId, List<Genre> newGenres) {
        ContentEntity content = contentRepository.findById(contentId)
                .orElseThrow(() -> new IllegalArgumentException("콘텐츠 ID가 유효하지 않습니다. id: " + contentId));

        // 기존 장르 삭제
        List<Genre> oldGenres = getGenresByContentId(contentId);  // 기존 장르 가져오기
        contentGenreRepository.deleteByContent(content);  // 기존 장르 삭제

        // 새로운 장르 추가
        for (Genre genre : newGenres) {
//            System.out.println(" update contentId :"+contentId);
//            System.out.println(" update content :"+content);
//            System.out.println(" update genre :"+genre);
//            System.out.println(" -----------------");

            ContentGenreEntity contentGenre = new ContentGenreEntity();
            contentGenre.setContent(content); // 반드시 content 세팅
            contentGenre.setGenre(genre);
            contentGenreRepository.save(contentGenre); // 새로운 장르 저장
            // 로그로 이전 장르와 수정된 장르 비교 (선택사항)
            System.out.println("Old Genres: " + oldGenres);
            System.out.println("New Genres: " + newGenres);

        }
    }

    /// ///////////////////////////////////////////////////////////////////////////////////////////
    /// 0825 video service 작성함
    /// ///////////////////////////////////////////////////////////////////////////////////////////
    // 1. 전체 조회 (Get all videos)
    public List<VideoDTO> getAllVideos() {
        // 모든 비디오 엔티티를 가져온 후, DTO로 변환하여 반환
        List<VideoEntity> videoEntities = videoRepository.findAll();
        return videoEntities.stream()
                .map(videoEntity -> modelMapper.map(videoEntity, VideoDTO.class))  // VideoEntity를 VideoDTO로 변환
                .collect(Collectors.toList());  // 결과를 리스트로 반환
    }

//    .map(videoEntity -> new VideoDTO(
//            videoEntity.getId(),
//    videoEntity.getTitle(),
//            videoEntity.getImage(),
//            videoEntity.getUrl(),
//            videoEntity.getContent().getId()
//))

    // ✔️ 목록용: 여러 영상 조회 (DTO 리스트)
    public List<VideoDTO> getVideoListByContentId(Integer contentId) {
        return videoRepository.findByContentId(contentId);
    }


    // 2. 개별 조회 (Get video by ID)
//    public VideoDTO getVideoById(Integer id) {
//        // 주어진 ID로 비디오 엔티티를 조회
//        Optional<VideoEntity> videoEntityOpt = videoRepository.findById(id);
//        if (videoEntityOpt.isEmpty()) {  // 비디오가 존재하지 않으면 예외 처리
//            throw new IllegalArgumentException("비디오 ID가 유효하지 않습니다. id: " + id);
//        }
//        return modelMapper.map(videoEntityOpt.get(), VideoDTO.class);  // DTO로 변환하여 반환
//    }
    // ✔️ 단일용: 콘텐츠에 연결된 첫 번째 영상 조회
    public VideoDTO getVideoByContentId(Integer contentId) {
        return videoRepository.findFirstByContentId(contentId)
                .map(video -> modelMapper.map(video, VideoDTO.class))
                .orElseThrow(() -> new NoSuchElementException("해당 콘텐츠의 영상이 존재하지 않습니다. contentId: " + contentId));
    }

    // 3. 입력 (Create new video)
    public VideoDTO createVideo(Integer contentId, VideoDTO dto) {
        VideoEntity entity = modelMapper.map(dto, VideoEntity.class);

        // ContentEntity 객체 생성 (contentId만 갖는 단순한 엔티티)
        ContentEntity contentEntity = contentRepository.findById(contentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 콘텐츠가 존재하지 않습니다. ID: " + contentId));
        entity.setContent(contentEntity); // 반드시 연결
        System.out.println(" createVideo contentId :"+contentId);

        return modelMapper.map(videoRepository.save(entity), VideoDTO.class);
    }

    // 4. 수정 (Update existing video)
    public VideoDTO updateVideo(Integer id, VideoDTO dto) {
        Optional<VideoEntity> existingOpt = videoRepository.findById(id);

        if (existingOpt.isEmpty()) {
            throw new NoSuchElementException("해당 비디오가 존재하지 않습니다. ID: " + id);
        }

        VideoEntity existing = existingOpt.get();

        existing.setTitle(dto.getTitle());
        existing.setUrl(dto.getUrl());
        existing.setImage(dto.getImage());

        return modelMapper.map(videoRepository.save(existing), VideoDTO.class);
    }
    // 5. 삭제 (Delete video)
    public void deleteVideo(Integer videoId) {
        // 주어진 videoId로 비디오 엔티티 조회
        VideoEntity videoEntity = videoRepository.findById(videoId)
                .orElseThrow(() -> new NoSuchElementException("삭제할 비디오를 찾을 수 없습니다. ID: " + videoId));

        // 비디오 엔티티 삭제
        videoRepository.delete(videoEntity);
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////
    // 별점 등록
    public void rateContent(Integer userId, Integer contentId, Integer score) {

        // 기존 별점 조회
        Optional<RatingEntity> existingRating = ratingRepository.findByUserIdAndContentId(userId, contentId);

        if (existingRating.isEmpty()) {
            RatingEntity ratingEntity = new RatingEntity();
            ratingEntity.setUser(userRepository.findById(userId).get());
            ratingEntity.setContent(contentRepository.findById(contentId).get());
            ratingEntity.setScore(score);
        } else {
            // 기존 별점이 있는 경우 업데이트
            RatingEntity ratingEntity = existingRating.get();
            ratingEntity.setScore(score);
        }
    }
}