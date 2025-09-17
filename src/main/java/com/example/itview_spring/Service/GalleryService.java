package com.example.itview_spring.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import com.example.itview_spring.DTO.GalleryDTO;
import com.example.itview_spring.Entity.ContentEntity;
import com.example.itview_spring.Entity.GalleryEntity;
import com.example.itview_spring.Repository.ContentRepository;
import com.example.itview_spring.Repository.GalleryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class GalleryService {

    private final GalleryRepository galleryRepository;
    private final ContentRepository contentRepository;
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

    /** 1. 콘텐츠 기준 갤러리 목록 조회 */
    @Transactional(readOnly = true)
    public List<GalleryDTO> getGallerysByContentId(Integer contentId) {
        // Repository에서 이미 DTO 반환 → map 제거
        return galleryRepository.findGallerysByContentId(contentId);

    }

    /** 2. 단일 갤러리 조회 */ //toDTO()는 이제 단일 조회 시만 사용
//    @Transactional(readOnly = true)
//    public GalleryDTO getGalleryById(Integer galleryId) {
//        GalleryEntity entity = galleryRepository.findById(galleryId)
//                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 Gallery ID: " + galleryId));
//        GalleryDTO dto = new GalleryDTO();
//        dto.setId(entity.getId());
//        dto.setContentId(entity.getContent().getId());
//        dto.setPhoto(entity.getPhoto());
//        return dto;
//    }
    @Transactional(readOnly = true)
    public GalleryDTO getGalleryById(Integer galleryId) {
        GalleryEntity entity = galleryRepository.findById(galleryId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 Gallery ID: " + galleryId));
        return toDTO(entity);
    }


    /** 3. 콘텐츠에 갤러리 추가 (중복 방지 포함) */
    @Transactional
    public GalleryDTO addGallery(Integer contentId, GalleryDTO dto) {
        ContentEntity content = contentRepository.findById(contentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 겔러리 ID: " + contentId));
        // Find the content entity by contentId, if not found throw exception

        // photo가 null이거나 빈 값일 경우 예외 처리
        if (dto.getPhoto() == null || dto.getPhoto().trim().isEmpty()) {
            System.out.println("gallery contentId  :" + contentId);
            System.out.println("gallery add        :" + dto.getPhoto());

            throw new IllegalArgumentException(" addGallery Service 사진 URL을 입력해주세요. ");
        }

        // 중복 사진 방지
        // Check if a gallery with the same photo already exists for this content
        if (galleryRepository.existsByContent_IdAndPhoto(contentId, dto.getPhoto())) {
            throw new IllegalArgumentException("이미 등록된 갤러리 사진입니다.");
        }
        // If a duplicate photo is found, throw an exception
        GalleryEntity gallery = new GalleryEntity();
        gallery.setContent(content); // Set the content for the gallery
        gallery.setPhoto(dto.getPhoto()); // Set the photo URL
        galleryRepository.save(gallery); // Save the gallery entity to the database

        return toDTO(gallery);
    }

    /**4.  갤러리 단건 수정 */
    @Transactional
    public  GalleryDTO updateGallery(Integer galleryId, GalleryDTO dto) {
        GalleryEntity gallery = galleryRepository.findById(galleryId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 Gallery ID: " + galleryId));

        gallery.setPhoto(dto.getPhoto());
        galleryRepository.save(gallery);

        return toDTO(gallery);
    }

    /** 5. 갤러리 전체 교체 */
    @Transactional
    public  List<GalleryDTO> replaceGalleries(Integer contentId, List<GalleryDTO> dtos) {
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

        return dtos;
    }
    /** 6. 갤러리 삭제 */
    @Transactional
    public void deleteGallery(Integer galleryId) {
        GalleryEntity gallery = galleryRepository.findById(galleryId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 Gallery ID: " + galleryId));
        // Find the gallery by galleryId, if not found throw exception
        galleryRepository.delete(gallery); // Delete the gallery from the database
    }
    /** 7. Entity -> DTO 변환 */
    private GalleryDTO toDTO(GalleryEntity entity) {
        GalleryDTO dto = new GalleryDTO();
        dto.setId(entity.getId());
        dto.setContentId(entity.getContent().getId());// contentId만 보관
        dto.setPhoto(entity.getPhoto());
        return dto;
    }
}
