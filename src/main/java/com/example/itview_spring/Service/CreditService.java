package com.example.itview_spring.Service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.itview_spring.DTO.CreditDTO;
import com.example.itview_spring.Repository.CreditRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CreditService {
    
    private final CreditRepository creditRepository;

    public Page<CreditDTO> getCreditByContentId(Pageable page, Integer contentId) {
        int currentPage = page.getPageNumber()-1;
        int pageSize = 3;

        Pageable pageable = PageRequest.of(currentPage, pageSize, Sort.by(Sort.Direction.ASC, "id"));
        
        return creditRepository.findByContentId(pageable, contentId);
    }
}
