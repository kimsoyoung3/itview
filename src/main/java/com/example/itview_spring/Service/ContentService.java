package com.example.itview_spring.Service;

import com.example.itview_spring.Constant.ActivityLogType;
import com.example.itview_spring.Constant.ContentType;
import com.example.itview_spring.DTO.*;
import com.example.itview_spring.Entity.*;
import com.example.itview_spring.Repository.*;
import com.example.itview_spring.Util.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional
public class ContentService {
    private final CommentService commentService;

    private final ContentRepository contentRepository;
    private final WishlistRepository wishlistRepository;
    private final ContentGenreRepository contentGenreRepository;
    private final GalleryRepository galleryRepository;
    private final ActivityLogRepository activityLogRepository;
    private final NotificationService notificationService;
    private final VideoRepository videoRepository;
    private final ExternalServiceRepository externalServiceRepository;
    private final RatingRepository ratingRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    private final S3Uploader s3Uploader;

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

    @Transactional
    public Page<ContentCreateDTO> getAllContents(String keyword, ContentType contentType, Pageable pageable) {
        int currentPage = pageable.getPageNumber();
        int pageLimits = 10;

        Pageable customizedPageable = PageRequest.of(currentPage, pageLimits, Sort.by(Sort.Direction.DESC, "id"));

        Page<ContentEntity> contentEntities;

        // 키워드나 타입이 제공되었는지 확인
        if ((keyword != null && !keyword.isEmpty()) || contentType != null) {
            // 검색 조건이 있으면 리포지토리의 검색 메소드 호출
            contentEntities = contentRepository.searchByKeywordAndType(keyword, contentType, customizedPageable);
        } else {
            // 검색 조건이 없으면 findAll() 메소드 호출
            contentEntities = contentRepository.findAll(customizedPageable);
        }

        Page<ContentCreateDTO> contentDTOS = contentEntities.map(entity -> modelMapper.map(entity, ContentCreateDTO.class));

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
    @Transactional
    public  AdminContentDTO read(Integer contentId) {
        //해당내용을 조회
        if (contentId == null) {
            throw new IllegalArgumentException("id는 null일 수 없습니다.");
        }
        Optional<ContentEntity> contentEntity = contentRepository.findById(contentId);
        if (contentEntity.isEmpty()) {
            throw new NoSuchElementException("해당 ID에 대한 콘텐츠를 찾을 수 없습니다: " + contentId);
        }
        // Entity를 DTO로 변환하여 반환
        return modelMapper.map(contentEntity, AdminContentDTO.class);
    }

    public ContentEntity findById(Integer id) {
        return contentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("콘텐츠를 찾을 수 없습니다."));
    }

    //등록(저장)
    //DTO를 받아서 저장
    //public void 내맘대로 (ProductDTO productDTO) {
    //public void create (ProductDTO productDTO) {  ex)

    public ContentCreateDTO create(ContentCreateDTO dto) {
        // ContentCreateDTO의 poster 필드는 이제 String 타입입니다.
        // AJAX 업로드를 통해 이미 URL이 String poster 필드에 담겨있습니다.

        // 1. DTO를 Entity로 변환 (String poster 필드가 ContentEntity의 String poster 필드로 자동 매핑됩니다.)
        ContentEntity contentEntity = modelMapper.map(dto, ContentEntity.class);

        // 2. Entity를 DB에 저장 (poster URL도 함께 저장됨)
        contentRepository.save(contentEntity);

        // 3. 저장된 Entity를 다시 DTO로 변환하여 반환
        return modelMapper.map(contentEntity, ContentCreateDTO.class);
    }


    //수정
    //주문번호와 DTO를 받아서, 주문번호로 조회해서 DTO의 내용을 저장
    // public void 수정할까(Integer orderId, ProductDTO productDTO) {
    // public void update(Integer orderId, ProductDTO productDTO) {   ex)

    /**
     * 콘텐츠 수정 (ID와 수정할 DTO를 받아서 수정)
     *
     * @param id  콘텐츠 ID
     * @param dto 수정할 콘텐츠 정보
     * @return ContentCreateDTO
     */
    public ContentCreateDTO update(Integer id, ContentCreateDTO dto) {

        ContentEntity contentEntity = contentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("콘텐츠 ID가 유효하지 않습니다: " + id));

        // --- 1. 포스터 URL 처리 (기존 로직 유지) ---
        String newPoster = dto.getPoster();
        String oldPoster = contentEntity.getPoster();

        if (StringUtils.hasText(oldPoster) && !Objects.equals(oldPoster, newPoster)) {
            // 기존 파일 삭제
            s3Uploader.deleteFile(oldPoster);
        }
        contentEntity.setPoster(StringUtils.hasText(newPoster) ? newPoster : null);
        // ------------------------------------------

        // 2. ModelMapper를 사용하여 나머지 모든 필드를 복사
        modelMapper.map(dto, contentEntity);

        // 3. 포스터 URL을 다시 명시적으로 설정 (ModelMapper가 String poster 필드도 덮어쓰므로 안전 조치)
        contentEntity.setPoster(StringUtils.hasText(newPoster) ? newPoster : null);

        // 4. 최종 저장
        contentRepository.save(contentEntity);

        return modelMapper.map(contentEntity, ContentCreateDTO.class);
    }

    //삭제
    //주문번호를 받아서 삭제
    //  public void 삭제가될까(Integer id) {
    //  public void delete(Integer id) {
    /**
     * 콘텐츠 삭제 (ID를 받아 해당 콘텐츠 삭제)
     *
     * @param id 콘텐츠 ID
     */

    @Transactional
    public void delete(Integer id) {
        ContentEntity content = contentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("콘텐츠 ID가 유효하지 않습니다. id: " + id));

        s3Uploader.deleteFile(content.getPoster());

        for (GalleryEntity gallery : content.getGalleries()) {
            s3Uploader.deleteFile(gallery.getPhoto());
        }

        contentRepository.delete(content);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // 0909 아래 holdind함
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // 컨텐츠 제목 조회
    public String getContentTitle(Integer contentId) {
        if (!contentRepository.existsById(contentId)) {
            throw new NoSuchElementException("존재하지 않는 컨텐츠입니다");
        }
        return contentRepository.findTitleById(contentId);
    }

    // 컨텐츠 제목 검색
    public Page<ContentDTO> searchContentByTitle(String title, int page) {
        Pageable pageable = PageRequest.of(page - 1, 10);

        Page<ContentEntity> contentPage = contentRepository.findByTitleContainingOrderByReleaseDateDesc(title, pageable);
        return contentPage.map(content -> modelMapper.map(content, ContentDTO.class));
    }

    // 컨텐츠 상세 정보 조회
    @Transactional
    public ContentDetailDTO getContentDetail(Integer contentId, Integer userId) {
        ContentDetailDTO contentDetail = new ContentDetailDTO();

        // 컨텐츠 정보 조회
        ContentResponseDTO contentResponseDTO = contentRepository.findContentWithAvgRating(contentId);
        if (contentResponseDTO == null) {
            throw new NoSuchElementException("존재하지 않는 컨텐츠입니다");
        }
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

        // 위시리스트 여부 조회
        Boolean wishlistCheck = wishlistRepository.existsByUserIdAndContentId(userId, contentId);
        contentDetail.setWishlistCheck(wishlistCheck);

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
    }

    // 컨텐츠의 코멘트 페이징 조회
    public Page<CommentDTO> getCommentsByContentId(Integer contentId, Integer userId, String order, int page) {
        if (!contentRepository.existsById(contentId)) {
            throw new NoSuchElementException("존재하지 않는 컨텐츠입니다");
        }
        Pageable pageable = PageRequest.of(page - 1, 3);
        return commentRepository.findByContentId(userId, contentId, order, pageable);
    }

    // 위시리스트 추가
    public void addWishlist(Integer userId, Integer contentId) {
        if (!userRepository.existsById(userId)) {
            throw new NoSuchElementException("존재하지 않는 유저입니다.");
        }
        if (!contentRepository.existsById(contentId)) {
            throw new NoSuchElementException("존재하지 않는 컨텐츠입니다.");
        }
        if (wishlistRepository.existsByUserIdAndContentId(userId, contentId)) {
            return;
        }

        WishlistEntity wishlistEntity = new WishlistEntity();
        wishlistEntity.setUser(userRepository.findById(userId).get());
        wishlistEntity.setContent(contentRepository.findById(contentId).get());
        wishlistRepository.save(wishlistEntity);

        ActivityLogEntity activityLog = new ActivityLogEntity();
        activityLog.setUser(wishlistEntity.getUser());
        activityLog.setType(ActivityLogType.WISH);
        activityLog.setReferenceId(wishlistEntity.getId());
        activityLog.setTimestamp(LocalDateTime.now());
        activityLogRepository.save(activityLog);

        // 팔로워들에게 알림 전송
        for (FollowEntity follow : userRepository.findById(userId).get().getFollowers()) {
            notificationService.sendNotification(follow.getFollower().getId());
        }
    }

    // 위시리스트 삭제
    public void removeWishlist(Integer userId, Integer contentId) {
        if (!userRepository.existsById(userId)) {
            throw new NoSuchElementException("존재하지 않는 유저입니다.");
        }
        if (!contentRepository.existsById(contentId)) {
            throw new NoSuchElementException("존재하지 않는 컨텐츠입니다.");
        }
        WishlistEntity wishlistEntity = wishlistRepository.findByUserIdAndContentId(userId, contentId);
        activityLogRepository.deleteByReferenceIdAndType(wishlistEntity.getId(), ActivityLogType.WISH);
        wishlistRepository.delete(wishlistEntity);
    }
}