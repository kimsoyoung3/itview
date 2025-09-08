package com.example.itview_spring.Controller.Content;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.itview_spring.DTO.GalleryDTO;
import com.example.itview_spring.Service.GalleryService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class GalleryController {

    private final GalleryService galleryService;

    /**
     * 갤러리 등록/수정 폼(GET)
     */
//    @GetMapping("/content/{contentId}/gallery")
//    public String galleryForm(@PathVariable("contentId") Integer contentId,
//                              Model model) {
//
//        // 콘텐츠 ID 전달 (필수)
//        model.addAttribute("contentId", contentId);
//
//        // 필요하다면 galleryList 조회 로직 추가
//        // model.addAttribute("galleryList", contentService.getGalleriesByContentId(contentId));
//
//        return "content/galleryForm"; // templates/content/galleryForm.html
//    }

//        @Autowired
//        public GalleryController(GalleryService galleryService, ContentService contentService) {
//            this.galleryService = galleryService;  // GalleryService 주입
//            this.contentService = contentService;  // ContentService 주입
//        }

    /**
     * 갤러리 등록/수정 폼 (GET)
     */
    @GetMapping("/content/{contentId}/gallery")
    public String galleryForm(@PathVariable("contentId") Integer contentId,
                              @RequestParam(value = "id", required = false) Integer galleryId,
                              Model model) {

        // 1️⃣ 단일 GalleryDTO 조회 (수정 모드)
        // galleryId가 있을 경우 기존 갤러리 정보를 수정 모드로 가져오고, 없으면 새로운 GalleryDTO를 생성
        GalleryDTO galleryDTO = (galleryId != null)
                ? galleryService.getGalleryById(galleryId)  // 갤러리 수정 모드일 경우 해당 갤러리 데이터를 조회
                : new GalleryDTO();                        // 신규 갤러리 등록 모드일 경우 새로운 객체 생성

        model.addAttribute("galleryDTO", galleryDTO);  // 모델에 갤러리 정보를 전달 (수정 또는 새로 추가된 갤러리 정보)

        // 2️⃣ 전체 GalleryDTO 리스트 조회 (목록)
        // contentId에 해당하는 모든 갤러리 목록을 조회
        List<GalleryDTO> galleryList = galleryService.getGallerysByContentId(contentId);
        model.addAttribute("galleryList", galleryList);  // 갤러리 목록을 모델에 전달

        // 3️⃣ ContentId도 모델에 전달
        model.addAttribute("contentId", contentId);  // contentId도 모델에 전달하여 폼에서 사용

        // "content/galleryForm" 템플릿을 반환하여 갤러리 등록/수정 폼을 렌더링
        return "content/galleryForm"; // 템플릿 경로: templates/content/galleryForm.html
    }

    /**
     * 갤러리 등록 또는 수정 처리 (POST)
     */
    @PostMapping("/content/{contentId}/gallery")
    public String createGallery(@PathVariable("contentId") Integer contentId,
                                @ModelAttribute GalleryDTO galleryDTO,
                                RedirectAttributes redirectAttributes) {

        // 1️⃣ 신규 갤러리 등록 or 기존 갤러리 수정 여부 판단
        if (galleryDTO.getId() == null) {  // galleryDTO에 ID가 없으면 신규 갤러리 등록
            galleryService.addGallery(contentId, galleryDTO); // 신규 갤러리 등록 처리
            redirectAttributes.addFlashAttribute("message", "갤러리가 등록되었습니다."); // 등록 성공 메시지
        } else {  // galleryDTO에 ID가 있으면 기존 갤러리 수정
            galleryService.updateGallery(galleryDTO.getId(), (List<GalleryDTO>) galleryDTO); // 갤러리 수정 처리
            redirectAttributes.addFlashAttribute("message", "갤러리가 수정되었습니다."); // 수정 성공 메시지
        }

        // 폼 제출 후 동일 contentId를 기준으로 갤러리 페이지로 리다이렉트
        redirectAttributes.addAttribute("contentId", contentId);  // 갤러리 페이지로 리다이렉트 시 contentId 전달
        return "redirect:/content/" + contentId + "/gallery";  // 갤러리 목록 페이지로 리다이렉트
    }

    /**
     * 갤러리 삭제 처리 (POST)
     */
    @PostMapping("/content/{contentId}/gallery/delete")
    public String deleteGallery(@PathVariable Integer contentId,
                                @RequestParam("galleryId") Integer galleryId,
                                RedirectAttributes redirectAttributes) {

        // 1️⃣ 갤러리 삭제 처리
        galleryService.deleteGallery(galleryId);  // galleryId에 해당하는 갤러리 삭제 처리

        // 2️⃣ 삭제 후 성공 메시지 추가
        redirectAttributes.addFlashAttribute("message", "갤러리가 삭제되었습니다.");  // 삭제 완료 메시지

        // 삭제 후 갤러리 목록 페이지로 리다이렉트
        return "redirect:/content/" + contentId + "/gallery";  // 삭제 후 갤러리 목록 페이지로 리다이렉트
    }

}
