package com.example.itview_spring.Controller.Content;

import java.util.List;

import com.example.itview_spring.DTO.GalleryDTOListWrapper;
import lombok.Data;
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

    //    1. 기능 흐름
//    기능명	URL	설명
//    콘텐츠 등록 폼 이동	GET /content/register	콘텐츠 등록 화면 이동
//    콘텐츠 등록 처리	POST /content/register	저장 후 장르 선택으로 리디렉트
//    콘텐츠 전체 조회	GET /content/list	페이지네이션 포함
//    콘텐츠 상세 보기	GET /content/detail?id={id}	contentId 기반 상세 정보
//    콘텐츠 수정 폼	GET /content/{id}/update	콘텐츠 수정 화면
//    콘텐츠 수정 처리	POST /content/{id}/update	저장 후 장르 수정으로 이동
//    콘텐츠 삭제	GET /content/delete?id={id}	삭제 후 리스트로 이동
//    장르 선택 폼	GET /content/{id}/genre	장르 선택 화면 표시
//    장르 저장 처리	POST /content/{id}/genre	저장 후 영상 등록으로 이동
//



//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////0910  gallery 수정 ////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

//    /** 갤러리 목록 조회 */
//    @GetMapping
//    public String getGalleryList(@PathVariable Integer contentId,
//                                 @RequestParam(value = "id", required = false) Integer galleryId,
//                                 Model model) {
//
//        // 전체 갤러리 목록
//        List<GalleryDTO> galleryList = galleryService.getGallerysByContentId(contentId);
//
//        // 전체 교체용 wrapper
//        GalleryDTOListWrapper wrapper = new GalleryDTOListWrapper();
//        wrapper.setGalleryDTOs(galleryList);
//        model.addAttribute("galleryDTOs", wrapper);
//
//
//        // 단건 조회 (수정 버튼 눌렀을 때)
//        GalleryDTO galleryDTO = (galleryId != null)
//                ? galleryService.getGalleryById(galleryId)
//                : new GalleryDTO();
//        model.addAttribute("galleryDTO", galleryDTO);
//        model.addAttribute("galleryList", galleryList);
//        model.addAttribute("contentId", contentId);
//
//        return "content/galleryForm"; // ✅ Thymeleaf 템플릿
//    }

    /**
     * 갤러리 등록/수정 폼 (GET)
     */
    @GetMapping("/content/{contentId}/gallery")
    public String galleryForm(@PathVariable("contentId") Integer contentId,
                                  @RequestParam(value = "id", required = false) Integer galleryId,
                                  Model model) {
        // ⃣ 전체 GalleryDTO 리스트 조회 (목록)
        // contentId에 해당하는 모든 갤러리 목록을 조회
        List<GalleryDTO> galleryList = galleryService.getGallerysByContentId(contentId);
        model.addAttribute("galleryList", galleryList);

        GalleryDTOListWrapper wrapper = new GalleryDTOListWrapper();
        wrapper.setGalleryDTOs(galleryList);
        model.addAttribute("galleryDTOs", wrapper);

        // 단건 조회 (수정 버튼 눌렀을 때)
        // galleryId가 있을 경우 기존 갤러리 정보를 수정 모드로 가져오고, 없으면 새로운 GalleryDTO를 생성
        GalleryDTO galleryDTO = (galleryId != null)
                ? galleryService.getGalleryById(galleryId)  // 갤러리 수정 모드일 경우 해당 갤러리 데이터를 조회
                : new GalleryDTO();                        // 신규 갤러리 등록 모드일 경우 새로운 객체 생성
        model.addAttribute("galleryDTO", galleryDTO);      // 수정용 // 모델에 갤러리 정보를 전달 (수정

        // 3️⃣ ContentId도 모델에 전달
        model.addAttribute("contentId", contentId);  // contentId도 모델에 전달하여 폼에서 사용

        // "content/galleryForm" 템플릿을 반환하여 갤러리 등록/수정 폼을 렌더링
        return "content/galleryForm"; // 템플릿 경로: templates/content/galleryForm.html
    }

    /**
     * 갤러리 단건 등록 또는 수정 처리 (POST)
     */
    @PostMapping("/content/{contentId}/gallery")
    public String createGallery(@PathVariable("contentId") Integer contentId,
                                @ModelAttribute GalleryDTO galleryDTO,
                                RedirectAttributes redirectAttributes) {

//        // 1️⃣ photo 필드 null/empty 체크
//         if (galleryDTO.getPhoto() == null  || galleryDTO.getPhoto().trim().isEmpty()) {
//             redirectAttributes.addFlashAttribute("message", "사진 URL을 입력해주세요.");
//             redirectAttributes.addAttribute("contentId", contentId);
//         return "redirect:/content/" + contentId + "/gallery";
//         }

        // 1️⃣ 신규 갤러리 등록 or 기존 갤러리 수정 여부 판단
        if (galleryDTO.getId() == null) {  // galleryDTO에 ID가 없으면 신규 갤러리 등록
            galleryService.addGallery(contentId, galleryDTO); // 신규 갤러리 등록 처리
            redirectAttributes.addFlashAttribute("message", "갤러리가 등록되었습니다."); // 등록 성공 메시지
        } else {  // galleryDTO에 ID가 있으면 기존 갤러리 수정
            galleryService.updateGallery(galleryDTO.getId(), galleryDTO); // 갤러리 수정 처리
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
        redirectAttributes.addAttribute("contentId", contentId);
        // 삭제 후 갤러리 목록 페이지로 리다이렉트
        return "redirect:/content/" + contentId + "/gallery";  // 삭제 후 갤러리 목록 페이지로 리다이렉트
    }

    /** 갤러리 전체 교체 */
    @PostMapping("/content/{contentId}/gallery/replace")
    public String replaceGalleries(@PathVariable Integer contentId,
                                   @ModelAttribute("galleryDTOs") GalleryDTOListWrapper wrapper,
                                   RedirectAttributes redirectAttributes) {
        if (wrapper.getGalleryDTOs() == null || wrapper.getGalleryDTOs().isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "갤러리 데이터가 없습니다!");
            return "redirect:/content/" + contentId + "/gallery";
        }

        galleryService.replaceGalleries(contentId, wrapper.getGalleryDTOs());
        redirectAttributes.addFlashAttribute("message", "갤러리가 전체 교체되었습니다!");
        redirectAttributes.addAttribute("contentId", contentId);
        return "redirect:/content/" + contentId + "/gallery";
    }
}
