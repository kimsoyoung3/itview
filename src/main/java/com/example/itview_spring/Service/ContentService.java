package com.example.itview_spring.Service;

import com.example.itview_spring.DTO.AdminContentDTO;
import com.example.itview_spring.DTO.ContentDetailDTO;
import com.example.itview_spring.DTO.ContentResponseDTO;
import com.example.itview_spring.DTO.ExternalServiceDTO;
import com.example.itview_spring.DTO.GenreDTO;
import com.example.itview_spring.DTO.ImageDTO;
import com.example.itview_spring.DTO.VideoDTO;
import com.example.itview_spring.Entity.ContentEntity;
import com.example.itview_spring.Entity.RatingEntity;
import com.example.itview_spring.Repository.ContentGenreRepository;
import com.example.itview_spring.Repository.ContentRepository;
import com.example.itview_spring.Repository.ExternalServiceRepository;
import com.example.itview_spring.Repository.GalleryRepository;
import com.example.itview_spring.Repository.RatingRepository;
import com.example.itview_spring.Repository.UserRepository;
import com.example.itview_spring.Repository.VideoRepository;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ContentService {
    private final ContentRepository contentRepository;
    private final ContentGenreRepository contentGenreRepository;
    private final GalleryRepository galleryRepository;
    private final VideoRepository videoRepository;
    private final ExternalServiceRepository externalServiceRepository;
    private final RatingRepository ratingRepository;
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

    public Page<AdminContentDTO> getAllContents(Pageable page) {
        int currentPage = page.getPageNumber()-1;
        int pageLimits = 10 ;

        Pageable pageable = PageRequest.of(currentPage,pageLimits, Sort.by(Sort.Direction.DESC, "id"));
        Page<ContentEntity> contentEntities = contentRepository.findAll(pageable) ;
        Page<AdminContentDTO> contentDTOS = contentEntities.map(data->modelMapper.map(
                data, AdminContentDTO.class));
        return contentDTOS;
    }
//    public List<AdminContentDTO> List() {
//        //읽기,수정/저장/삭제 ==>Repository
//        List<ContentEntity> contentEntities = contentRepository.findAll();
//        //Entity =있으면 밑에 DTO변환
//        List<AdminContentDTO> contentDTOs = Arrays.asList(modelMapper.map(contentEntities, AdminContentDTO[].class));
//        //DTO가 보이면 return DTO를지정
//        return contentDTOs;
//    }



    //상세보기,수정(개별조회)
    //주문번호를 받아서 해당하는 DTO에 전달
    //public ProductDTO 역시 안알려줌(Integer id) {
    //public ProductDTO read(Integer id) {    ex)
    public AdminContentDTO read(Integer id) {
        //해당내용을 조회
        if (id == null) {
            throw new IllegalArgumentException("id는 null일 수 없습니다.");
        }
        Optional<ContentEntity> contentEntity = contentRepository.findById(id);
        if (contentEntity.isEmpty()) {
            throw new NoSuchElementException("해당 ID에 대한 콘텐츠를 찾을 수 없습니다: " + id);
        }
        AdminContentDTO adminContentDTO = modelMapper.map(contentEntity.get(), AdminContentDTO.class);
        return adminContentDTO;
    }

    //등록(저장)
    //DTO를 받아서 저장
    //public void 내맘대로 (ProductDTO productDTO) {
    //public void create (ProductDTO productDTO) {  ex)
    public AdminContentDTO create(AdminContentDTO dto) {
        //DTO가 이있으면 반드시 Entity 변환
     //   System.out.println("dto:"+dto);
        ContentEntity contentEntity = modelMapper.map(dto, ContentEntity.class);
     //   System.out.println("entity:"+ContentEntity);
        contentRepository.save(contentEntity);
        return modelMapper.map(contentEntity, AdminContentDTO.class);
    }

    //수정
    //주문번호와 DTO를 받아서, 주문번호로 조회해서 DTO의 내용을 저장
    // public void 수정할까(Integer orderId, ProductDTO productDTO) {
    // public void update(Integer orderId, ProductDTO productDTO) {   ex)
    public AdminContentDTO update(Integer id, AdminContentDTO dto) {
        //해당내용찾기
//        System.out.println("dto:"+dto);
        ContentEntity contentEntity = modelMapper.map(dto, ContentEntity.class);

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
        return modelMapper.map(contentEntity, AdminContentDTO .class);
    }

    //삭제
    //주문번호를 받아서 삭제
    //  public void 삭제가될까(Integer id) {
    //  public void delete(Integer id) {
    public boolean delete(Integer id) {
        if(contentRepository.existsById(id)) { //데이터가 존재하면
            contentRepository.deleteById(id); //삭제
            return true;
        }
        return false;
    }

    // 컨텐츠 상세 정보 조회
    public ContentDetailDTO getContentDetail(Integer contentId) {
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

        return contentDetail;
    }

    // 별점 등록
    public void rateContent(Integer userId, Integer contentId, Integer score) {

        // 기존 별점 조회
        Optional<RatingEntity> existingRating = ratingRepository.findByUserIdAndContentId(userId, contentId);

        if (existingRating.isEmpty()) {
            RatingEntity ratingEntity = new RatingEntity();
            ratingEntity.setUser(userRepository.findById(userId).get());
            ratingEntity.setContent(contentRepository.findById(contentId).get());
            ratingEntity.setScore(score);
        }
        else {
            // 기존 별점이 있는 경우 업데이트
            RatingEntity ratingEntity = existingRating.get();
            ratingEntity.setScore(score);
        }        
    }
}
