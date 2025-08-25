package com.example.itview_spring.Service;


import com.example.itview_spring.DTO.*;
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

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class VideoService {
    private final VideoRepository videoRepository;
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
//    }



    //상세보기,수정(개별조회)
    //주문번호를 받아서 해당하는 DTO에 전달
    //public ProductDTO 역시 안알려줌(Integer id) {
    //public ProductDTO read(Integer id) {    ex)
    public VideoDTO read(Integer id) {
        //해당내용을 조회
        if (id == null) {
            throw new IllegalArgumentException("id는 null일 수 없습니다.");
        }
        Optional<VideoEntity> videoEntity = videoRepository.findById(id);
        if (videoEntity.isEmpty()) {
            throw new NoSuchElementException("해당 ID에 대한 콘텐츠를 찾을 수 없습니다: " + id);
        }
        VideoDTO adminVideoDTO = modelMapper.map(videoEntity.get(), VideoDTO.class);
        return adminVideoDTO;
    }

    //등록(저장)
    //DTO를 받아서 저장
    //public void 내맘대로 (ProductDTO productDTO) {
    //public void create (ProductDTO productDTO) {  ex)
    public VideoDTO create(VideoDTO dto) {
        //DTO가 이있으면 반드시 Entity 변환

        VideoEntity videoEntity = modelMapper.map(dto, VideoEntity.class);
        System.out.println("service add dto:"+dto);
        System.out.println("service add entity:"+videoEntity);

        videoRepository.save(videoEntity);
        return modelMapper.map(videoEntity, VideoDTO.class);
    }

    //수정
    //주문번호와 DTO를 받아서, 주문번호로 조회해서 DTO의 내용을 저장
    // public void 수정할까(Integer orderId, ProductDTO productDTO) {
    // public void update(Integer orderId, ProductDTO productDTO) {   ex)
    public VideoDTO update(Integer id, VideoDTO dto) {
        //해당내용찾기
//        System.out.println("dto:"+dto);
        VideoEntity videoEntity = modelMapper.map(dto, VideoEntity.class);

        if (videoEntity == null) {
            return null;
        }
//        System.out.println("entity:"+videoEntity);
        //내용을 저장(@Id가 있는 변수는 저장 불가)
        videoEntity.setTitle(dto.getTitle());

        videoRepository.save(videoEntity);
        return modelMapper.map(videoEntity, VideoDTO .class);
    }

    //삭제
    //주문번호를 받아서 삭제
    //  public void 삭제가될까(Integer id) {
    //  public void delete(Integer id) {
    @Transactional
    public void delete(Integer id) {
        VideoEntity video = videoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("콘텐츠 ID가 유효하지 않습니다. id: " + id));

        // Now delete the video entity
        videoRepository.delete(video);
    }

}





