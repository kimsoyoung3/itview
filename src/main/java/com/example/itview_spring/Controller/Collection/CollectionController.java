package com.example.itview_spring.Controller.Collection;

import com.example.itview_spring.DTO.AdminCollectionDTO;
import com.example.itview_spring.DTO.AdminContentDTO;
import com.example.itview_spring.Service.CollectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class CollectionController {
    private final CollectionService collectionService;

    @GetMapping("/collection/list")
    public String listAll(@RequestParam(value = "keyword", required = false) String keyword,
                          @PageableDefault(page = 0, size = 10) Pageable pageable,
                          Model model) {

        Page<AdminCollectionDTO> collectionsPage = collectionService.list(keyword, pageable);

        // 페이지 블록 계산
        int currentPage = collectionsPage.getNumber();
        int blockSize = 10;
        int startPage = (int) (Math.floor(currentPage / blockSize) * blockSize);
        int endPage = Math.min(startPage + blockSize - 1, collectionsPage.getTotalPages() - 1);

        // 모델에 데이터 추가
        model.addAttribute("collections", collectionsPage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "collection/list";
    }

    @DeleteMapping("/collection/{collectionId}")
    public String delete(@PathVariable("collectionId") int id,
                                   RedirectAttributes redirectAttributes) {

        collectionService.delete(id);
        redirectAttributes.addFlashAttribute("message", "컬렉션이 성공적으로 삭제되었습니다.");

        return "redirect:/collections/list";
    }

    @GetMapping("/collection/{collectionId}")
    public String read(@PathVariable("collectionId") Integer collectionId,
                       @PageableDefault(size = 10) Pageable pageable,
                       Model model) {

        // 1. 컬렉션 상세 정보 조회
        AdminCollectionDTO collectionDetail = collectionService.getCollectionDetail(collectionId);

        // 2. 컬렉션에 속한 콘텐츠 목록 조회 (페이징 정보도 포함)
        Page<AdminContentDTO> contentsPage = collectionService.getContentsByCollectionId(collectionId, pageable);

        // 3. 페이징 UI에 필요한 startPage와 endPage 계산
        int startPage = Math.max(0, contentsPage.getNumber() - 2);
        int endPage = Math.min(contentsPage.getTotalPages() - 1, contentsPage.getNumber() + 2);

        // 4. 모델에 데이터 추가
        model.addAttribute("collection", collectionDetail);
        model.addAttribute("contents", contentsPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "collection/detail";
    }
}
