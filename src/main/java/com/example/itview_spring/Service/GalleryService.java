package com.example.itview_spring.Service;


import com.example.itview_spring.Constant.ContentType;
import com.example.itview_spring.DTO.*;
import com.example.itview_spring.Entity.ContentEntity;
import com.example.itview_spring.Entity.CreditEntity;
import com.example.itview_spring.Entity.GalleryEntity;
import com.example.itview_spring.Entity.PersonEntity;
import com.example.itview_spring.Repository.*;
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
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class GalleryService {

    private final ContentGenreRepository contentGenreRepository;
    private final ContentRepository contentRepository;
    private final CreditRepository creditRepository;

    private final GalleryRepository galleryRepository;  //0904 추가
    private final PersonRepository personRepository;
    private final ExternalServiceRepository externalServiceRepository;
    private final ModelMapper modelMapper;

    /**
     * 겔러리 기준 페이징 조회
     */
    public Page<CreditDTO> getCreditByContentId(Pageable page, Integer contentId) {
        int currentPage = page.getPageNumber() - 1;
        int pageSize = 12;

        Pageable pageable = PageRequest.of(currentPage, pageSize, Sort.by(Sort.Direction.ASC, "id"));
        return creditRepository.findByContentId(pageable, contentId);
    }

//    itview-spring/
//            ├─src/main/java/com/example/itview_spring/
//            │  ├─Controller/
//            │  │   └─Content/
//            │  │       └─CreditController.java
//            │  ├─DTO/
//                        │  │   ├─CreditDTO.java
//            │  │   └─PersonDTO.java
//            │  ├─Entity/
//                        │  │   ├─ContentEntity.java
//            │  │   ├─CreditEntity.java
//            │  │   └─PersonEntity.java
//            │  ├─Repository/
//                        │  │   ├─ContentRepository.java
//            │  │   ├─CreditRepository.java
//            │  │   └─PersonRepository.java
//            │  ├─Service/
//                        │  │   └─CreditService.java
//            │  └─ItviewSpringApplication.java
//            ├─src/main/resources/templates/content/
//                        │  └─creditForm.html
//            ├─src/main/resources/application.properties
//            └─pom.xml


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
//    특정 겔러리와 연결된 새 갤러리를 추가합니다. 먼저 동일한 URL을 가진 갤러리가 photo이미 있는지 확인합니다.contentId중복을 방지하기 위해
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
     * 갤러리 추가
     *///

    /** 겔러리 목록 조회 */
    @Transactional(readOnly = true)
    public List<GalleryDTO> getGallerysByContentId(Integer contentId) {
        return galleryRepository.findGallerysByContentId(contentId)
                .stream()
                .map(this::toDTO)  // ✅ Entity -> DTO 변환
                .collect(Collectors.toList());
    }

    /** 단일 갤러리 조회 */
    @Transactional(readOnly = true)
    public GalleryDTO getGalleryById(Integer galleryId) {
        return galleryRepository.findById(galleryId)
                .map(this::toDTO)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 Gallery ID: " + galleryId));
    }

    /** 갤러리 단건 등록 (중복 방지 포함) */
    @Transactional
    public void addGallery(Integer contentId, GalleryDTO dto) {
        ContentEntity content = contentRepository.findById(contentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 겔러리 ID: " + contentId));
        // Find the content entity by contentId, if not found throw exception
        // 중복 사진 방지
        // Check if a gallery with the same photo already exists for this content
        if (galleryRepository.existsByContentIdAndPhoto(contentId, dto.getPhoto())) {
            throw new IllegalArgumentException("이미 등록된 갤러리 사진입니다.");
        }
        // If a duplicate photo is found, throw an exception
        GalleryEntity gallery = new GalleryEntity();

        gallery.setContent(content); // Set the content for the gallery
        gallery.setPhoto(dto.getPhoto()); // Set the photo URL
//        entity.setContent(content);
//        entity.setPhoto(dto.getPhoto());
        galleryRepository.save(gallery); // Save the gallery entity to the database
    }

    /** 갤러리 단건 수정 */
    @Transactional
    public void updateGallery(Integer galleryId, GalleryDTO dto) {
        GalleryEntity gallery = galleryRepository.findById(dto.getId())
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 Gallery ID: " + galleryId));

        gallery.setPhoto(dto.getPhoto());
        galleryRepository.save(gallery);
    }

    /**
     * ✅ 갤러리 전체 교체
     */
    @Transactional
    public void replaceGalleries(Integer contentId, List<GalleryDTO> dtos) {
        ContentEntity content = contentRepository.findById(contentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 Content ID: " + contentId));

        // 기존 갤러리 안전하게 제거 후 새로 등록
        content.getGalleries().clear();  // ✅ 컬렉션 직접 clear
        for (GalleryDTO dto : dtos) {
            GalleryEntity gallery = new GalleryEntity();
            gallery.setContent(content);
            gallery.setPhoto(dto.getPhoto());
            content.getGalleries().add(gallery); // ✅ 컬렉션 직접 add
        }
        contentRepository.save(content);
    }
    /**
     * 갤러리 삭제
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
        dto.setId(entity.getId());
        dto.setContentId(entity.getContent().getId());// contentId만 보관
        dto.setPhoto(entity.getPhoto());
        return dto;
    }
}
