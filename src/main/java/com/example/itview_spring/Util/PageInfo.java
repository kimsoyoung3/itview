package com.example.itview_spring.Util;

import com.example.itview_spring.DTO.PageInfoDTO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

/**
 * 페이지 처리를 위한 로직
 */
@Component
public class PageInfo {
    /**
     * Page 객체를 받아 페이지네이션 정보를 PageInfoDTO로 변환
     */
    public PageInfoDTO getPageInfo(Page<?> page) {
        int totalPages = page.getTotalPages();

        // 전체 페이지가 0이면 빈 페이지 정보 리턴
        if (totalPages == 0) {
            return new PageInfoDTO(0, 0, 0, 0, 0, 0);
        }

        int current = page.getNumber() + 1; // 0-based → 1-based
        int blockLimit = 10;
        int halfBlock = blockLimit / 2;

        int start = Math.max(1, current - halfBlock);
        int end = start + blockLimit - 1;

        if (end > totalPages) {
            end = totalPages;
            start = Math.max(1, end - blockLimit + 1);
        }

        int prev = Math.max(1, current - 1);
        int next = Math.min(totalPages, current + 1);
        int last = totalPages;

        return new PageInfoDTO(start, end, prev, current, next, last);
    }
}
