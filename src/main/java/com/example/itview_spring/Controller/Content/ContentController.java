package com.example.itview_spring.Controller.Content;

import com.example.itview_spring.DTO.*;
import com.example.itview_spring.Service.ContentService;
import com.example.itview_spring.Util.PageInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.NoSuchElementException;

@Controller
@RequiredArgsConstructor
public class ContentController {
    //  private final VideoService videoService;      //Video 서비스
    //  private final GalleryService galleryService;  // 갤러리 서비스
    @Autowired
    private final ContentService contentService;  // 콘텐츠 서비스

    private final PageInfo pageInfo;

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


    // ==================== CONTENT CRUD ==========================
// 등록 폼 이동
    @GetMapping("/content/register")
    public String newContent() {

        return "content/register";
    }

    // 등록 처리 후 → 장르 선택 페이지로 이동
    @PostMapping("/content/register")
    public String newContent(ContentCreateDTO contentDTO, RedirectAttributes redirectAttributes) {
        //데이터 저장처리 (Service -> Controller
        ContentCreateDTO savedContent = contentService.create(contentDTO);// 저장 후
        redirectAttributes.addFlashAttribute("message", "콘텐츠가 성공적으로 등록되었습니다.");

        System.out.println(" 00 savedContent ==" + savedContent);
        return "redirect:/content/" + savedContent.getId() + "/genre";     // 장르 등록 폼으로 이동
        //   return "redirect:/content/list";  // 리스트 이동 옵션 (현재는 장르 선택 우선)
    }


    //localhost:8080/content/list 확인함(@GetMppping
    //전체강의 조회후 (list.html)로이동

    /**
     * 전체조회
     *
     * @param pageable 조회할 페이지 번호 /페이징 정보 (기본 페이지 1)
     * @param model    결좌 전달 /뷰에 전달할 모델
     * @return 페이지 이동 /뷰
     */
    @GetMapping("/content/list")
    // 전체 목록 조회
    public String listContent(@PageableDefault(page = 1) Pageable pageable, Model model) {
        //모든 데이터를 조회
        //keyword  추가 ****
        Page<ContentCreateDTO> contentDTOS = contentService.getAllContents(pageable);
        model.addAttribute("contentDTOS", contentDTOS);
        //    System.out.println("contentDTO.            ==", contentDTOS);

        // 페이지 정보 생성 후 모델에 추가
        PageInfoDTO pageInfoDTO = pageInfo.getPageInfo(contentDTOS);
        model.addAttribute("pageInfoDTO", pageInfoDTO);
//        model.addAttribute("contentDTOS", contentDTOS);
//        model.addAttribute("pageInfoDTO", pageInfo.getPageInfo(contentDTOS));
        return "content/list";
    }

    // 상세 보기 (id 파라미터로 받음, @PathVariable 대신 @RequestParam 형식)
// @GetMapping("/content/{id:\\d+}")
//    @GetMapping("/content/detail")
//    @GetMapping("/content/{id}/detail")
//    public String detailContent(@PathVariable("id") Integer id, Model model) {
    @GetMapping("/content/detail")
    public String detailContent(Integer id, Model model) {
        //0909 수정처리함
        try {
            ContentCreateDTO contentDTO = contentService.read(id);
            model.addAttribute("contentDTO", contentDTO);
            return "content/detail"; // Renders content detail page
        } catch (NoSuchElementException e) {
            model.addAttribute("error", "해당 콘텐츠를 찾을 수 없습니다.");
            return "error/404"; // 또는 에러 페이지로 리디렉션
        }
//        // URL 경로 변수인 {id}를 받으려면 @PathVariable을 써야 합니다.
//        ContentCreateDTO contentDTO = contentService.read(id);
//        model.addAttribute("contentDTO", contentDTO);
//        System.out.println("deteil id         ===>" + id);
//        System.out.println("deteil contentDTO ===>" + contentDTO);
//        return "content/detail"; // 경로가 정확한지 확인 필요
    }

    // 수정 폼 이동
    @GetMapping("/content/{id}/update")
    public String updateContent(@PathVariable("id") Integer id, Model model) {
        ContentCreateDTO contentDTO = contentService.read(id);
        model.addAttribute("contentDTO", contentDTO);
        return "content/update";
    }

    // 수정 처리 (→ 장르 수정 화면으로 리디렉트) //0909 수정처리함
    @PostMapping("/content/{id}/update")
    public String updateContentProc(@PathVariable("id") Integer id, ContentCreateDTO contentDTO, RedirectAttributes redirectAttributes) {
        try {
            // 콘텐츠 수정 처리
            ContentCreateDTO savedContent = contentService.update(id, contentDTO);

            // 수정 완료 메시지 추가 (옵션)
            redirectAttributes.addFlashAttribute("message", "콘텐츠가 성공적으로 수정되었습니다.");

            // 장르 수정 페이지로 리디렉션 (장르 수정 폼으로 이동)
            return "redirect:/content/" + savedContent.getId() + "/genre";
        } catch (NoSuchElementException e) {
            // 콘텐츠가 존재하지 않으면 에러 메시지 추가 후, 오류 페이지로 리디렉션
            redirectAttributes.addFlashAttribute("error", "콘텐츠를 수정할 수 없습니다.");
            return "redirect:/error";
        }
    }
//    @PostMapping("/content/{id}/update")
//    public String updateContentProc(@PathVariable("id") Integer id, ContentCreateDTO contentDTO) {
//
//        // Service에서 수정 처리 후 저장된 DTO 반환 받음
//        ContentCreateDTO savedContent = contentService.update(id, contentDTO);
////        System.out.println(" 22 savedContent ==" + savedContent);
//        // 수정 후 바로 장르 수정 페이지로 이동
//        return "redirect:/content/" + savedContent.getId() + "/genre";     // 장르 수정 폼으로 이동
//    }

    // 삭제 처리 (id를 @RequestParam 으로 받음)
    @GetMapping("/content/delete")
    public String deleteContent(@RequestParam("id") Integer id) {
        contentService.delete(id);
        return "redirect:/content/list";
    }
}