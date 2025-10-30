package com.example.itview_spring.Service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.itview_spring.DTO.ExternalServiceDTO;
import com.example.itview_spring.Entity.ContentEntity;
import com.example.itview_spring.Entity.ExternalServiceEntity;
import com.example.itview_spring.Repository.ContentRepository;
import com.example.itview_spring.Repository.ExternalServiceRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ExternalService {

    private final ExternalServiceRepository externalServiceRepository;
    private final ContentRepository contentRepository;
    
// 1. 전체 조회 (Get all external_services contentId 기준 모든영상)
// ✔️ 목록용: 여러 영상 조회 (DTO 리스트)
    @Transactional(readOnly = true)
    public List<ExternalServiceDTO> getExternalServicesByContentId(Integer contentId) {

        return externalServiceRepository.findByContentId(contentId); // Repository에서 DTO 바로 반환
    }
// 2. 개별 조회 (Get external_serviceId기준 )

    // 단일 ExternalServiceDTO 조회
    @Transactional(readOnly = true)
    public ExternalServiceDTO getExternalServiceById(Integer externalServiceId) {
        return externalServiceRepository.findById(externalServiceId)
                .map(v -> new ExternalServiceDTO(
                        v.getId(),
                                            v.getType(),   // Channel 타입
                                            v.getHref()    // 링크 URL
                ))
                .orElse(null);
    }
//
    // 3. 입력 (Create new externalService)
    @Transactional
    public ExternalServiceDTO createExternalService(Integer contentId, ExternalServiceDTO externalServiceDTO) {

        ExternalServiceEntity entity = new ExternalServiceEntity();
        // ✅ DTO 필드와 Entity 필드 매핑
        entity.setType(externalServiceDTO.getType()); // Channel enum
        entity.setHref(externalServiceDTO.getHref()); // URL

        // ✅ URL에서 넘어온 contentId 활용
        ContentEntity contentEntity = contentRepository.findById(contentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 외부 서비스가 존재하지 않습니다. ID: " + contentId));
        entity.setContent(contentEntity);

        externalServiceRepository.save(entity);
        // ✅ DTO 반환 시 필드 맞춤
        return new ExternalServiceDTO(entity.getId(), entity.getType(), entity.getHref());
    }

    // 4. 수정 (Update existing externalService)
    @Transactional
    public ExternalServiceDTO updateExternalService(Integer id, ExternalServiceDTO dto) {
        ExternalServiceEntity entity = externalServiceRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 외부 서비스가 존재하지 않습니다. ID: " + id));

        // DTO → Entity 매핑
        entity.setType(dto.getType());  // Channel enum
        entity.setHref(dto.getHref());  // URL

        externalServiceRepository.save(entity);
        // Entity → DTO 반환
        return new ExternalServiceDTO(entity.getId(), entity.getType(), entity.getHref());
    }

    // 5. 삭제 (Delete externalService)
    @Transactional
    public void deleteExternalService(Integer externalServiceId) {
        if (!externalServiceRepository.existsById(externalServiceId)) {
            throw new NoSuchElementException(
                    "삭제할 외부 서비스를 찾을 수 없습니다. ID: " + externalServiceId
            );
        }

        externalServiceRepository.deleteById(externalServiceId);
    }
}
