package com.example.itview_spring.Controller.User;

import com.example.itview_spring.Constant.Genre;
import com.example.itview_spring.DTO.*;
import com.example.itview_spring.Entity.*;
import com.example.itview_spring.Repository.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional
public class ContentService1 {
    private final CreditRepository creditRepository;
    private final CommentService commentService;
    private final PersonRepository personRepository;

    private final ContentRepository contentRepository;
    private final ContentGenreRepository contentGenreRepository;
    private final GalleryRepository galleryRepository;
//    @Autowired
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
    @Transactional
    public Page<ContentCreateDTO> getAllContents(Pageable page) {
        int currentPage = page.getPageNumber() - 1;
        int pageLimits = 10;

        Pageable pageable = PageRequest.of(currentPage, pageLimits, Sort.by(Sort.Direction.DESC, "id"));
        Page<ContentEntity> contentEntities = contentRepository.findAll(pageable);
        System.out.println("총 페이지 수: " + contentEntities.getTotalPages());
        System.out.println("총 컨텐츠 수: " + contentEntities.getTotalElements());
        System.out.println("현재 페이지 번호: " + contentEntities.getNumber());

        for (ContentEntity content : contentEntities.getContent()) {
            System.out.println("--------------------------------");
            System.out.println("ID: " + content.getId());
            System.out.println("Title: " + content.getTitle());
            System.out.println("Type: " + content.getContentType());
            System.out.println("Release Date: " + content.getReleaseDate());
            System.out.println("Poster: " + content.getPoster());
            System.out.println("Nation: " + content.getNation());
            System.out.println("Channel: " + content.getChannelName());
            System.out.println("Genres: " + content.getGenres());
            System.out.println("외부 서비스: " + content.getVideos());
        }
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
    @Transactional
    public  ContentCreateDTO read(Integer id) {
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
    @Transactional
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
    @Transactional
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
        Pageable pageable = PageRequest.of(page - 1, 1);
        return commentRepository.findByContentId(userId, contentId, order, pageable);
    }


    /////////////////////////////////////////////////////////////////////////////////////////////
    /// 0901 gallery //////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////
//    1.getGallerysByContentId:
//
//    특정 항목과 관련된 모든 갤러리를 가져옵니다 contentId.
//
//    2.getGalleryById:
//
//    단일 갤러리를 검색하여 galleryId`GalleryDTO`로 변환합니다.GalleryDTO. 갤러리를 찾을 수 없는 경우
//
//    3.addGallery:
//
//    특정 콘텐츠와 연결된 새 갤러리를 추가합니다. 먼저 동일한 URL을 가진 갤러리가 photo이미 있는지 확인합니다.contentId중복을 방지하기 위해
//
//    4.updateGallery:
//
//    주어진 모든 기존 갤러리를 삭제한 contentId다음 새 갤러리를 추가합니다.
//
//    5.deleteGallery:
//
//            . 으로 식별된 갤러리를 삭제합니다 galleryId.
//
//            toDTO:
//
//    6.GalleryEntity객체를 `Gallery`로 변환합니다 .GalleryDTO객체, 그것을 적합하게 만들기
//

    /**
     * 콘텐츠에 갤러리 추가
     *///

    /** 콘텐츠 기준 갤러리 목록 조회 */
    @Transactional(readOnly = true)
    public List<GalleryDTO> getGallerysByContentId(Integer contentId) {
        return galleryRepository.findGallerysByContentId(contentId);
        // Retrieve all galleries associated with the given contentId
    }

    /** 단일 갤러리 조회 */
    @Transactional(readOnly = true)
    public GalleryDTO getGalleryById(Integer galleryId) {
        return galleryRepository.findById(galleryId)
                .map(this::toDTO) // Convert entity to DTO if found
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 Gallery ID: " + galleryId));
        // If gallery is not found, throw an exception with a clear message
    }

    /** 콘텐츠에 갤러리 추가 (중복 방지 포함) */
    @Transactional
    public void addGallery(Integer contentId, GalleryDTO galleryDTO) {
        ContentEntity content = contentRepository.findById(contentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 콘텐츠 ID: " + contentId));
        // Find the content entity by contentId, if not found throw exception

        // Check if a gallery with the same photo already exists for this content
        if (galleryRepository.existsByContentIdAndPhoto(contentId, galleryDTO.getPhoto())) {
            throw new IllegalArgumentException("이미 등록된 갤러리 사진입니다.");
        }
        // If a duplicate photo is found, throw an exception
        GalleryEntity gallery = new GalleryEntity();
        gallery.setContent(content); // Set the content for the gallery
        gallery.setPhoto(galleryDTO.getPhoto()); // Set the photo URL

        galleryRepository.save(gallery); // Save the gallery entity to the database
    }


    /** 콘텐츠 갤러리 수정 (전체 삭제 후 새로 등록) */
    @Transactional
    public void updateGallery(Integer contentId, List<GalleryDTO> galleryDTOs) {
        // Delete existing galleries for the content if needed
        galleryRepository.deleteByContentId(contentId);

        // Loop through the provided list of new gallery DTOs and save them
        for (GalleryDTO galleryDTO : galleryDTOs) {
            ContentEntity content = contentRepository.findById(contentId)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 콘텐츠 ID: " + contentId));
            // Retrieve the content entity by contentId

            GalleryEntity gallery = new GalleryEntity();
            gallery.setContent(content);  // Set the content for the gallery
            gallery.setPhoto(galleryDTO.getPhoto()); // Set the photo URL

            galleryRepository.save(gallery); // Save each new gallery to the database
        }
    }
    /**
     * Gallery 삭제
     */
    @Transactional
    public void deleteGallery(Integer galleryId) {
        GalleryEntity gallery = galleryRepository.findById(galleryId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 Gallery ID: " + galleryId));
        // Find the gallery by galleryId, if not found throw exception
        galleryRepository.delete(gallery); // Delete the gallery from the database
    }
    /**
     * Entity -> DTO 변환
     */
    private GalleryDTO toDTO(GalleryEntity entity) {
        GalleryDTO dto = new GalleryDTO();
        dto.setId(entity.getId());  // Set the gallery ID
        dto.setContentId(entity.getContent().getId()); // Set the related content's ID
        dto.setPhoto(entity.getPhoto());  // Set the gallery photo URL
        return dto;  // Return the GalleryDTO object
    }

    /// ///////////////////////////////////////////////////////////////////////////////////////////
    /// 0825 video service 작성함
    /// ///////////////////////////////////////////////////////////////////////////////////////////
    // 1. 전체 조회 (Get all videos contentId 기준 모든영상)
    // ✔️ 목록용: 여러 영상 조회 (DTO 리스트)
    @Transactional(readOnly = true)
    public List<VideoDTO> getVideosByContentId(Integer contentId) {
//        // 1️⃣ VideoEntity 리스트 조회
//        List<VideoEntity> videos = videoRepository.findByContent_Id(contentId);
//
//        // 2️⃣ VideoEntity -> VideoDTO 변환
//        List<VideoDTO> videoDTOs = videos.stream()
//                .map(v -> new VideoDTO(v.getId(), v.getTitle(), v.getImage(), v.getUrl()))
//                .toList();
////        List<VideoEntity> videos = videoRepository.findByContent_Id(contentId);
//        List<VideoDTO> videoDTOS = Arrays.asList(modelMapper.map(videos, VideoDTO[].class));
//        return videoDTOs;
        return videoRepository.findByContentId(contentId); // Repository에서 DTO 바로 반환
    }
//    VideoRepository에 이미 JPQL로 List<VideoDTO> findByContentId(Integer contentId)를 정의해두셨으므로,
//    return videoRepository.findByContentId(contentId); //처럼 한 줄로 바로 반환할 수도 있습니다.

//    // 2. 개별 조회 (Get videoId기준 )
//
    // 단일 VideoDTO 조회
    @Transactional(readOnly = true)
    public VideoDTO getVideoById(Integer videoId) {
//        return videoRepository.findById(videoId)
//                .map(v -> new VideoDTO(v.getId(), v.getTitle(), v.getImage(), v.getUrl()))
//                .orElse(null);
        VideoEntity videoEntity = videoRepository.findById(videoId)
                .orElseThrow(() -> new NoSuchElementException("해당 영상이 존재하지 않습니다: " + videoId));

        // VideoEntity -> VideoDTO로 변환
        return new VideoDTO(videoEntity.getId(), videoEntity.getTitle(), videoEntity.getImage(), videoEntity.getUrl());
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
//        entity.setContentId(contentId); // VideoEntity에 contentId 연동


        // ✅ URL에서 넘어온 contentId 활용
        ContentEntity contentEntity = contentRepository.findById(contentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 콘텐츠가 존재하지 않습니다. ID: " + contentId));
        entity.setContent(contentEntity);
        System.out.println(" createVideo contentId :" + contentId);

        videoRepository.save(entity);

        return new VideoDTO(entity.getId(), entity.getTitle(), entity.getImage(), entity.getUrl());
    }

    // 4. 수정 (Update existing video)
    @Transactional
    public VideoDTO updateVideo(Integer id, VideoDTO dto) {
        VideoEntity entity = videoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 외부 서비스가 존재하지 않습니다. ID: " + id));

        entity.setTitle(dto.getTitle());
        entity.setImage(dto.getImage());
        entity.setUrl(dto.getUrl());
        videoRepository.save(entity);
        return new VideoDTO(entity.getId(), entity.getTitle(), entity.getImage(), entity.getUrl());
    }

    // 5. 삭제 (Delete video)
    @Transactional
    public void deleteVideo(Integer videoId) {
        // 주어진 videoId로 외부 서비스 엔티티 조회
        VideoEntity videoEntity = videoRepository.findById(videoId)
                .orElseThrow(() -> new NoSuchElementException("삭제할 외부 서비스를 찾을 수 없습니다. ID: " + videoId));

        // 외부 서비스 엔티티 삭제
        videoRepository.delete(videoEntity);
    }

}