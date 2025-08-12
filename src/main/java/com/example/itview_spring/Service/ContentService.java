package com.example.itview_spring.Service;

import com.example.itview_spring.DTO.ContentDTO;
import com.example.itview_spring.Entity.ContentEntity;
import com.example.itview_spring.Repository.ContentRepository;
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
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ContentService {
    //반드시 사용할 Repository (방금 작업한 파일)와 MOdelMapper추가
    private final ContentRepository contentRepository;
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

    public Page<ContentDTO> getAllContents(Pageable page) {
        int currentPage = page.getPageNumber()-1;
        int pageLimits = 10 ;

        Pageable pageable = PageRequest.of(currentPage,pageLimits, Sort.by(Sort.Direction.DESC, "id"));
        Page<ContentEntity> contentEntities = contentRepository.findAll(pageable) ;
        Page<ContentDTO> contentDTOS = contentEntities.map(data->modelMapper.map(
                data, ContentDTO.class));
        return contentDTOS;
    }
//    public List<ContentDTO> List() {
//        //읽기,수정/저장/삭제 ==>Repository
//        List<ContentEntity> contentEntities = contentRepository.findAll();
//        //Entity =있으면 밑에 DTO변환
//        List<ContentDTO> contentDTOs = Arrays.asList(modelMapper.map(contentEntities, ContentDTO[].class));
//        //DTO가 보이면 return DTO를지정
//        return contentDTOs;
//    }



    //상세보기,수정(개별조회)
    //주문번호를 받아서 해당하는 DTO에 전달
    //public ProductDTO 역시 안알려줌(Integer id) {
    //public ProductDTO read(Integer id) {    ex)
    public ContentDTO read(Integer id) {
        //해당내용을 조회
        Optional<ContentEntity> contentEntity = contentRepository.findById(id);
        ContentDTO contentDTO = modelMapper.map(contentEntity.get(), ContentDTO.class);
        return contentDTO;
    }

    //등록(저장)
    //DTO를 받아서 저장
    //public void 내맘대로 (ProductDTO productDTO) {
    //public void create (ProductDTO productDTO) {  ex)
    public ContentDTO create(ContentDTO dto) {
        //DTO가 이있으면 반드시 Entity 변환
     //   System.out.println("dto:"+dto);
        ContentEntity contentEntity = modelMapper.map(dto, ContentEntity.class);
     //   System.out.println("entity:"+ContentEntity);
        contentRepository.save(contentEntity);
        return modelMapper.map(contentEntity, ContentDTO.class);
    }

    //수정
    //주문번호와 DTO를 받아서, 주문번호로 조회해서 DTO의 내용을 저장
    // public void 수정할까(Integer orderId, ProductDTO productDTO) {
    // public void update(Integer orderId, ProductDTO productDTO) {   ex)
    public ContentDTO update(ContentDTO dto) {
        //해당내용찾기
        ContentEntity contentEntity = modelMapper.map(dto, ContentEntity.class);

        if (contentEntity == null) {
            return null;
        }
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
        return modelMapper.map(contentEntity, ContentDTO.class);
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
}
