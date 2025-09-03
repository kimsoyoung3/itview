package com.example.itview_spring.Service;


import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.itview_spring.Constant.ContentType;
import com.example.itview_spring.DTO.CreditDTO;
import com.example.itview_spring.DTO.WorkDTO;
import com.example.itview_spring.DTO.WorkDomainDTO;
import com.example.itview_spring.Repository.ContentRepository;
import com.example.itview_spring.Repository.CreditRepository;
import com.example.itview_spring.Repository.ExternalServiceRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CreditService {
    
    private final CreditRepository creditRepository;
    private final ContentRepository contentRepository;
    private final ExternalServiceRepository externalServiceRepository;

    public Page<CreditDTO> getCreditByContentId(Pageable page, Integer contentId) {
        if (!contentRepository.existsById(contentId)) {
            throw new NoSuchElementException("Invalid contentId: " + contentId);
        }
        int currentPage = page.getPageNumber()-1;
        int pageSize = 12;

        Pageable pageable = PageRequest.of(currentPage, pageSize, Sort.by(Sort.Direction.ASC, "id"));
        
        return creditRepository.findByContentId(pageable, contentId);
    }

    // 인물의 작품 참여 분야 조회
    public List<WorkDomainDTO> getWorkDomainsByPersonId(Integer personId) {
        if (!creditRepository.existsById(personId)) {
            throw new IllegalArgumentException("Invalid personId: " + personId);
        }
        return creditRepository.findWorkDomainsByPersonId(personId);
    }

    // 분야별 페이징 조회
    public Page<WorkDTO> getWorks(Integer page, Integer personId, ContentType contentType, String department) {
        if (!creditRepository.existsById(personId)) {
            throw new IllegalArgumentException("Invalid personId: " + personId);
        }
        Pageable pageable = PageRequest.of(page - 1, 6);
        Page<WorkDTO> workDTOPage = creditRepository.findWorkDTOPage(pageable, personId, contentType, department);
        for (WorkDTO workDTO : workDTOPage) {
            workDTO.setExternalServices(externalServiceRepository.findByContentId(workDTO.getId()));
        }
        return workDTOPage;
    }
}
