package com.example.itview_spring.Service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import com.example.itview_spring.DTO.GalleryDTO;
import com.example.itview_spring.Entity.ContentEntity;
import com.example.itview_spring.Entity.GalleryEntity;
import com.example.itview_spring.Repository.ContentRepository;
import com.example.itview_spring.Repository.GalleryRepository;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@Transactional
public class GalleryService {

    private final GalleryRepository galleryRepository;
    private final ContentRepository contentRepository;
    
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
}
