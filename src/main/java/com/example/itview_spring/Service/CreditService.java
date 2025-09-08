package com.example.itview_spring.Service;


import java.util.List;
import java.util.NoSuchElementException;

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
public class CreditService {
    
    private final CreditRepository creditRepository;
    private final ContentRepository contentRepository;
    private final ExternalServiceRepository externalServiceRepository;
    private final PersonRepository personRepository;

    public Page<CreditDTO> getCreditByContentId(Pageable page, Integer contentId) {
        if (!contentRepository.existsById(contentId)) {
            throw new NoSuchElementException("존재하지 않는 컨텐츠입니다");
        }
        int currentPage = page.getPageNumber()-1;
        int pageSize = 12;

        Pageable pageable = PageRequest.of(currentPage, pageSize, Sort.by(Sort.Direction.ASC, "id"));
        
        return creditRepository.findByContentId(pageable, contentId);
    }

    // 인물의 작품 참여 분야 조회
    public List<WorkDomainDTO> getWorkDomainsByPersonId(Integer personId) {
        if (!creditRepository.existsById(personId)) {
            throw new NoSuchElementException("존재하지 않는 인물입니다");
        }
        return creditRepository.findWorkDomainsByPersonId(personId);
    }

    // 분야별 페이징 조회
    public Page<WorkDTO> getWorks(Integer page, Integer personId, ContentType contentType, String department) {
        if (!creditRepository.existsById(personId)) {
            throw new NoSuchElementException("존재하지 않는 인물입니다");
        }
        Pageable pageable = PageRequest.of(page - 1, 6);
        Page<WorkDTO> workDTOPage = creditRepository.findWorkDTOPage(pageable, personId, contentType, department);
        for (WorkDTO workDTO : workDTOPage) {
            workDTO.setExternalServices(externalServiceRepository.findByContentId(workDTO.getId()));
        }
        return workDTOPage;
    }

    /**
     * 콘텐츠에 크레딧 추가
     *///

//    public CreditService(CreditRepository creditRepository,
//                         ContentRepository contentRepository,
//                         PersonRepository personRepository) {
//        this.creditRepository = creditRepository;
//        this.contentRepository = contentRepository;
//        this.personRepository = personRepository;
//    }
    /** 콘텐츠 기준 크레딧 목록 조회 */
    @Transactional(readOnly = true)
    public List<CreditDTO> getCreditsByContentId(Integer contentId) {
        return creditRepository.findCreditsByContentId(contentId);
    }

    /** 단일 크레딧 조회 */
    @Transactional(readOnly = true)
    public CreditDTO getCreditById(Integer creditId) {
        CreditDTO creditDTO = creditRepository.findCreditById(creditId);
        if (creditDTO == null) {
            throw new NoSuchElementException("존재하지 않는 크레딧 ID: " + creditId);
        }
        return creditDTO;
    }

    /** Person 이름 기준 조회 후 없으면 생성 */
    @Transactional
    public PersonEntity getOrCreatePersonByName(String name) {
        PersonEntity person = personRepository.findByName(name);
        if (person == null) {
            person = new PersonEntity();
            person.setName(name);
            person.setJob("Unknown"); // 기본값
            personRepository.save(person);
        }
        return person;
    }
    
    /** 콘텐츠에 크레딧 추가 (중복 방지 포함) */
    public void addCredits(Integer contentId, List<CreditDTO> credits) {
        ContentEntity content = contentRepository.findById(contentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 콘텐츠 ID: " + contentId));

        List<CreditDTO> existingCredits = creditRepository.findCreditsByContentId(contentId);

        for (CreditDTO creditDTO : credits) {
            // 디버깅 출력
            System.out.println("📝 addCredits 진입 - creditDTO: " + creditDTO);
            if (creditDTO.getPerson() != null){
                System.out.println("📝 person.id: " + creditDTO.getPerson().getId());
                System.out.println("📝 person.name: " + creditDTO.getPerson().getName());
            } else {
                System.out.println("⚠️ creditDTO.getPerson()가 null입니다.");
            }

            if (creditDTO.getPerson() == null || creditDTO.getPerson().getId() == null) {
                throw new IllegalArgumentException("Person 정보가 필요합니다.");
            }

            boolean alreadyExists = existingCredits.stream()
                    .anyMatch(c -> c.getPerson().getId().equals(creditDTO.getPerson().getId()) &&
                            c.getDepartment().equals(creditDTO.getDepartment()) &&
                            c.getRole().equals(creditDTO.getRole()));

            if (!alreadyExists) {
                saveCredit(content, creditDTO);
            }
        }
    }

    /** 콘텐츠 크레딧 수정 (전체 삭제 후 새로 등록) */

    public void updateCredits(Integer contentId, List<CreditDTO> newCredits) {
        ContentEntity content = contentRepository.findById(contentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 콘텐츠 ID: " + contentId));

        creditRepository.deleteAllByContent_Id(contentId);

        for (CreditDTO creditDTO : newCredits) {
            saveCredit(content, creditDTO);
        }
    }
    /** 단일 크레딧 수정 */
    public void updateCredit(Integer contentId, List<CreditDTO> newCredits) {
        ContentEntity content = contentRepository.findById(contentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 콘텐츠 ID: " + contentId));

        creditRepository.deleteAllByContent_Id(contentId);

        for (CreditDTO creditDTO : newCredits) {
            saveCredit(content, creditDTO);
        }
    }

    /** 내부 공통 저장 메서드 */
    private void saveCredit(ContentEntity content, CreditDTO creditDTO) {
        if (creditDTO.getPerson() == null || creditDTO.getPerson().getId() == null) {
            throw new IllegalArgumentException("Person 정보가 필요합니다.");
        }

        PersonEntity person = personRepository.findById(creditDTO.getPerson().getId())
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 인물 ID: " + creditDTO.getPerson().getId()));

        CreditEntity creditEntity = new CreditEntity();
        creditEntity.setContent(content);
        creditEntity.setPerson(person);
        creditEntity.setDepartment(creditDTO.getDepartment());
        creditEntity.setRole(creditDTO.getRole());
        creditEntity.setCharacterName(creditDTO.getCharacterName());

        creditRepository.save(creditEntity);
    }
    /** 크레딧 삭제 */
    public void deleteCredit(Integer creditId) {
        CreditEntity credit = creditRepository.findById(creditId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 크레딧 ID: " + creditId));
        creditRepository.delete(credit);
    }
}
