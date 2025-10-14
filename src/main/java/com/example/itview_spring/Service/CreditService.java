package com.example.itview_spring.Service;


import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import com.example.itview_spring.DTO.PersonDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.itview_spring.Constant.ContentType;
import com.example.itview_spring.DTO.CreditDTO;
import com.example.itview_spring.DTO.WorkDTO;
import com.example.itview_spring.DTO.WorkDomainDTO;
import com.example.itview_spring.Entity.ContentEntity;
import com.example.itview_spring.Entity.CreditEntity;
import com.example.itview_spring.Entity.PersonEntity;
import com.example.itview_spring.Repository.ContentRepository;
import com.example.itview_spring.Repository.CreditRepository;
import com.example.itview_spring.Repository.ExternalServiceRepository;
import com.example.itview_spring.Repository.PersonRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CreditService {

    private final CreditRepository creditRepository;
    private final ContentRepository contentRepository;
    private final PersonRepository personRepository;
    private final ExternalServiceRepository externalServiceRepository;

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

    ///////////////////////////////////////////////////////////////////////////////////////////
    // 0908 ///
    //////////////////////////////////////////////////////////////////////////////////////////
    // 1. 콘텐츠 ID로 크레딧 목록 조회 (페이징을 고려한 결과를 처리하는 방법으로 변경)
    /**
     * 콘텐츠 ID로 크레딧 조회
     */
    // 1. 콘텐츠 ID로 크레딧 목록 조회 (페이징을 고려한 결과를 처리하는 방법으로 변경)
    // 0910 holding
    /**
     * 전체 조회
     */
    @Transactional(readOnly = true)
    public List<CreditDTO> getCreditByContentId(Integer contentId) {
        if (!contentRepository.existsById(contentId)) {
            throw new NoSuchElementException("존재하지 않는 콘텐츠입니다: " + contentId);
        }
        return creditRepository.findByContentId(contentId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    // ✅ 콘텐츠 ID로 페이징 조회
    @Transactional(readOnly = true)
    public Page<CreditDTO> getCreditByContentId(Pageable pageable, Integer contentId) {
        if (!contentRepository.existsById(contentId)) {
            throw new NoSuchElementException("존재하지 않는 컨텐츠입니다");
        }
        // ✅ Repository가 이미 DTO를 반환하므로 mapToDTO 필요 없음
        return creditRepository.findByContentId(pageable, contentId);
    }

    // 3. 개별 크레딧 조회 (CreditId 기준)
    /**
     * 개별 크레딧 조회
     */
    @Transactional(readOnly = true)
    public CreditDTO getCreditById(Integer creditId) {
        CreditEntity entity = creditRepository.findById(creditId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 크레딧 ID: " + creditId));
        return mapToDTO(entity);
    }
    // 4. 입력 (Create)
    /**
     * 신규 생성
     */
    @Transactional
    public CreditDTO addCredit(Integer contentId, CreditDTO creditDTO) {
        // 콘텐츠 존재 여부 확인
        ContentEntity content = contentRepository.findById(contentId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 콘텐츠 ID: " + contentId));
        // 인물 존재 여부 확인 (없으면 새로 생성)
        PersonEntity person;
        if (creditDTO.getPerson() != null && creditDTO.getPerson().getId() != null) {
            // 기존 인물 사용
            person = personRepository.findById(creditDTO.getPerson().getId())
                    .orElseThrow(() -> new NoSuchElementException(
                            "존재하지 않는 인물 ID: " + creditDTO.getPerson().getId()));
        } else if (creditDTO.getPerson() != null && creditDTO.getPerson().getName() != null) {
            // 이름으로 신규 생성 또는 기존 인물 가져오기
            person = getOrCreatePersonByName(creditDTO.getPerson().getName());
        } else {
            throw new IllegalArgumentException("인물 정보가 필요합니다.");
        }
//        // 인물 존재 여부 확인
//        PersonEntity person = personRepository.findById(creditDTO.getPerson().getId())
//                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 인물 ID: " + creditDTO.getPerson().getId()));
//
//        // 중복된 크레딧 존재 여부 확인 (Content와 Person이 동일한 경우)
//        if (creditRepository.existsByContentAndPerson(content, person)) {
//            throw new IllegalArgumentException("이 콘텐츠와 인물에 대한 크레딧이 이미 존재합니다.");
//        }

        // 새로운 크레딧 엔티티 생성 및 저장
        CreditEntity entity = new CreditEntity();
        entity.setContent(content);
        entity.setPerson(person);
        entity.setDepartment(creditDTO.getDepartment());
        entity.setRole(creditDTO.getRole());
        entity.setCharacterName(creditDTO.getCharacterName());

        creditRepository.save(entity);

        return mapToDTO(entity);
    }

//    PersonEntity person;
//if (creditDTO.getPerson().getId() != null) {
//        person = personRepository.findById(creditDTO.getPerson().getId())
//                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 인물 ID: " + creditDTO.getPerson().getId()));
//    } else if (creditDTO.getPerson().getName() != null) {
//        person = getOrCreatePersonByName(creditDTO.getPerson().getName());
//    } else {
//        throw new IllegalArgumentException("인물 정보가 없습니다.");
//    }


    // 5. 수정 (Update)
    /**
     * 수정
     */
    @Transactional
    public CreditDTO updateCredit(Integer creditId, CreditDTO creditDTO) {
        // 기존 크레딧 조회
        CreditEntity entity = creditRepository.findById(creditId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 크레딧 ID: " + creditId));

        boolean updated = false;
        // 기존 값과 비교 후 변경 시만 반영
        if (creditDTO.getDepartment() != null && !creditDTO.getDepartment().equals(entity.getDepartment())) {
            entity.setDepartment(creditDTO.getDepartment());
            updated = true;
        }
        if (creditDTO.getRole() != null && !creditDTO.getRole().equals(entity.getRole())) {
            entity.setRole(creditDTO.getRole());
            updated = true;
        }
        if (creditDTO.getCharacterName() != null && !creditDTO.getCharacterName().equals(entity.getCharacterName())) {
            entity.setCharacterName(creditDTO.getCharacterName());
            updated = true;
        }

        // Person 정보 수정
        if (creditDTO.getPerson() != null && creditDTO.getPerson().getId() != null
                && !creditDTO.getPerson().getId().equals(entity.getPerson().getId())) {
            PersonEntity person = personRepository.findById(creditDTO.getPerson().getId())
                    .orElseThrow(() -> new NoSuchElementException("존재하지 않는 인물 ID: " + creditDTO.getPerson().getId()));
            entity.setPerson(person);
            updated = true;
        }

        if (updated) {
            creditRepository.save(entity);
        }

        return mapToDTO(entity);
    }

    // 6. 삭제 (Delete)
    /**
     * 삭제
     */
    @Transactional
    public void deleteCredit(Integer creditId) {
        // 기존 크레딧 조회
        creditRepository.findById(creditId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 크레딧 ID: " + creditId));

        // 크레딧 삭제
        creditRepository.deleteById(creditId);
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////0911 인물검색하여 처리하는 것 추가////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // 🔹 인물 이름 검색 (부분 일치)
    @Transactional(readOnly = true)
    public List<PersonDTO> searchPersons(String keyword) {

        if(keyword == null || keyword.trim().isEmpty()) {
            return List.of(); // 빈 리스트 반환
        }

        return personRepository.findByNameContainingIgnoreCase(keyword)
                .stream()
                .map(p -> new PersonDTO(
                        p.getId(),
                        p.getName(),
                        p.getProfile(),
                        p.getJob()
                ))
                .collect(Collectors.toList());
    }

    // ==========================
    // 🔹 Person 조회 / 생성
    // ==========================
    @Transactional(readOnly = true)
    public PersonEntity getOrCreatePersonByName(String name) {
        // 이름이 null일 경우 예외 처리
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("인물 이름은 필수입니다.");
        }

        // 기존 인물 조회
        PersonEntity person = personRepository.findByName(name);
        if (person == null) {
            person = new PersonEntity();
            person.setName(name);
            person.setJob("Unknown"); // 기본값
            personRepository.save(person);
        }
        return person;
    }
    // ==========================
    // DTO 변환 메서드
    // ==========================
    private CreditDTO mapToDTO(CreditEntity creditEntity) {
        return new CreditDTO(
                creditEntity.getId(),
                new PersonDTO(
                        creditEntity.getPerson().getId(),
                        creditEntity.getPerson().getName(),
                        creditEntity.getPerson().getProfile(),
                        creditEntity.getPerson().getJob()
                ),
                creditEntity.getCharacterName(),
                creditEntity.getDepartment(),
                creditEntity.getRole()
        );
    }

    ////0911 인물검색하여 처리하는 것 추가 끝 //////////////////////////////////////////////////////////////


















    ///////////////////////////////////////////////////////////////////////////////////////////////
    /// ///////////////////////////////////////////////////////////////////////////////////////
    /// ///////////////////////////////////////////////////////////////////////////////////////
    /// ///////////////////////////////////////////////////////////////////////////////////////

    // 콘텐츠 ID로 페이징 조회
    public Page<CreditDTO> getCreditsByContentId(Pageable page, Integer contentId) {
        if (!contentRepository.existsById(contentId)) {
            throw new NoSuchElementException("존재하지 않는 컨텐츠입니다");
        }
        int currentPage = page.getPageNumber() - 1;
        int pageSize = 12;

        Pageable pageable = PageRequest.of(currentPage, pageSize, Sort.by(Sort.Direction.ASC, "id"));

        return creditRepository.findByContentId(pageable, contentId);
    }

    // 인물의 작품 참여 분야 조회
    public List<WorkDomainDTO> getWorkDomainsByPersonId(Integer personId) {
        if (!personRepository.existsById(personId)) {
            throw new NoSuchElementException("존재하지 않는 인물입니다");
        }
        return creditRepository.findWorkDomainsByPersonId(personId);
    }

    // 분야별 페이징 조회
    public Page<WorkDTO> getWorks(Integer page, Integer personId, ContentType contentType, String department) {
        if (!personRepository.existsById(personId)) {
            throw new NoSuchElementException("존재하지 않는 인물입니다");
        }
        Pageable pageable = PageRequest.of(page - 1, 6);
        Page<WorkDTO> workDTOPage = creditRepository.findWorkDTOPage(pageable, personId, contentType, department);
        for (WorkDTO workDTO : workDTOPage) {
            workDTO.setExternalServices(externalServiceRepository.findByContentId(workDTO.getId()));
        }
        return workDTOPage;
    }
}