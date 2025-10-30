package com.example.itview_spring.Service;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.itview_spring.DTO.GalleryDTO;
import com.example.itview_spring.Entity.ContentEntity;
import com.example.itview_spring.Entity.GalleryEntity;
import com.example.itview_spring.Repository.ContentRepository;
import com.example.itview_spring.Repository.GalleryRepository;
import com.example.itview_spring.Util.S3Uploader;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class GalleryService {

    private final GalleryRepository galleryRepository;
    private final ContentRepository contentRepository;
    private final S3Uploader s3Uploader;

    /** 1. 콘텐츠 기준 갤러리 목록 조회 */
    @Transactional(readOnly = true)
    public List<GalleryDTO> getGallerysByContentId(Integer contentId) {
        return galleryRepository.findGallerysByContentId(contentId);
    }

    /** 2. 갤러리 단건 조회 */
    @Transactional(readOnly = true)
    public GalleryDTO getGalleryById(Integer galleryId) {
        GalleryEntity entity = galleryRepository.findById(galleryId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 Gallery ID: " + galleryId));
        return toDTO(entity);
    }

    /** 3. 갤러리 추가 (S3 업로드 및 DB 저장) */
    @Transactional
    public GalleryDTO addGallery(Integer contentId, MultipartFile file) throws IOException {
        ContentEntity content = contentRepository.findById(contentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 콘텐츠 ID: " + contentId));

        String photoUrl = s3Uploader.uploadFile(file);

        if (galleryRepository.existsByContent_IdAndPhoto(contentId, photoUrl)) {
            s3Uploader.deleteFile(photoUrl);
            throw new IllegalArgumentException("이미 등록된 갤러리 사진입니다.");
        }

        GalleryEntity gallery = new GalleryEntity();
        gallery.setContent(content);
        gallery.setPhoto(photoUrl);
        galleryRepository.save(gallery);

        return toDTO(gallery);
    }

    /** 4. 갤러리 단건 수정 (이 메서드는 사용하지 않습니다) */
    @Transactional
    public GalleryDTO updateGallery(Integer galleryId, GalleryDTO dto) {
        GalleryEntity gallery = galleryRepository.findById(galleryId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 Gallery ID: " + galleryId));

        gallery.setPhoto(dto.getPhoto());
        galleryRepository.save(gallery);

        return toDTO(gallery);
    }

    /** 5. 갤러리 삭제 (S3 파일 삭제 포함) */
    @Transactional
    public void deleteGallery(Integer galleryId) {
        GalleryEntity gallery = galleryRepository.findById(galleryId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 Gallery ID: " + galleryId));

        s3Uploader.deleteFile(gallery.getPhoto());
        galleryRepository.delete(gallery);
    }

    /** 6. Entity -> DTO 변환 */
    private GalleryDTO toDTO(GalleryEntity entity) {
        GalleryDTO dto = new GalleryDTO();
        dto.setId(entity.getId());
        dto.setContentId(entity.getContent().getId());
        dto.setPhoto(entity.getPhoto());
        return dto;
    }
}