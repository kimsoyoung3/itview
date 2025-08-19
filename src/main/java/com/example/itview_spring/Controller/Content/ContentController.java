package com.example.itview_spring.Controller.Content;

import com.example.itview_spring.Constant.Genre;
import com.example.itview_spring.DTO.ContentCreateDTO;
import com.example.itview_spring.DTO.PageInfoDTO;
import com.example.itview_spring.Service.ContentService;
import com.example.itview_spring.Util.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ContentController {
    private final ContentService contentService;
    private final PageInfo pageInfo;

    // ==================== ERROR PAGE HANDLER ====================
    //    500 에러가 나더라도 커스텀 오류 페이지 추가 가능 (선택 사항)-----------------------------------
//    @Controller
//    public class CustomErrorController implements ErrorController {
//        @RequestMapping("/error")
//        public String handleError() {
//            return "error/customError"; // templates/error/customError.html
//        }
//    }
// ==================== CONTENT CRUD ==========================
// 등록 폼 이동
    @GetMapping("/content/register")
    public String newContent() {

        return "content/register";
    }
    // 등록 처리 후 → 장르 선택 페이지로 이동
    @PostMapping("/content/register")
    public String newContent(ContentCreateDTO contentDTO) {
        //데이터 저장처리 (Service -> Controller
        ContentCreateDTO savedContent = contentService.create(contentDTO);
        return "redirect:/content/" + savedContent.getId() + "/genre";
        //   return "redirect:/content/list";
    }


    //localhost:8080/content/list 확인함(@GetMppping
    //전체강의 조회후 (list.html)로이동
    /**
     * 전체조회
     *
     * @param pageable 조회할 페이지 번호
     * @param model    결좌 전달
     * @return 페이지 이동
     */
    @GetMapping("/content/list")
    // 전체 목록 조회
    public String listContent(@PageableDefault(page = 1) Pageable pageable, Model model) {
        //모든 데이터를 조회
        //keyword  추가 ****
        Page<ContentCreateDTO> contentDTOS = contentService.getAllContents(pageable);
        model.addAttribute("contentDTOS", contentDTOS);
    //    System.out.printf("contentDTO.            ==", contentDTOS);

        PageInfoDTO pageInfoDTO = pageInfo.getPageInfo(contentDTOS);
        model.addAttribute("pageInfoDTO", pageInfoDTO);
//        model.addAttribute("contentDTOS", contentDTOS);
//        model.addAttribute("pageInfoDTO", pageInfo.getPageInfo(contentDTOS));
        return "content/list";
    }

    // 상세 보기
    @GetMapping("/content/{id}")
    public String detailContent(@PathVariable("id") Integer id, Model model) {
        // URL 경로 변수인 {id}를 받으려면 @PathVariable을 써야 합니다.
        ContentCreateDTO contentDTO = contentService.read(id);
        model.addAttribute("contentDTO", contentDTO);
        //     System.out.printf("contentDTO.getContentId==",contentDTO.getContentId());
        //     System.out.printf("contentDTO.            ==",contentDTO);
        return "content/detail";
    }

    // 수정 폼
    @GetMapping("/content/{id}/update")
    public String updateContent(@PathVariable("id") Integer id, Model model) {
        ContentCreateDTO contentDTO = contentService.read(id);
        model.addAttribute("contentDTO", contentDTO);
        return "content/update";
    }

    // 수정 처리
    @PostMapping("/content/{id}/update")
    public String updateContentProc(@PathVariable("id") Integer id, ContentCreateDTO contentDTO) {
        contentService.update(id, contentDTO);
        return "redirect:/content/list";
    }

    //삭제처리
    @GetMapping("/content/delete")
    public String deleteContent(@RequestParam("id") Integer id) {
        contentService.delete(id);
        return "redirect:/content/list";
    }

    // ==================== GENRE HANDLING ========================
    //콘텐츠 장르 등록/수정 페이지 {/content/{id}/genre)
    // 장르 선택/수정 폼
    @GetMapping("/content/{contentId}/genre")
    public String showGenreForm(@PathVariable("contentId") Integer contentId, Model model) {
        List<Genre> allGenres = Arrays.asList(Genre.values());
       // List<Genre> selectedGenres = List.of(Genre.DRAMA); // 예시로 DRAMA만 선택됨
        List<Genre> selectedGenres = contentService.getGenresByContentId(contentId);

        model.addAttribute("allGenres", allGenres);
        model.addAttribute("selectedGenres", selectedGenres);
        model.addAttribute("contentId", contentId); // 중요
        return "content/genreForm";
    }
    // 장르 저장
    @PostMapping("/content/{contentId}/genre")
    public String submitGenres(@PathVariable("contentId") Integer contentId,
                               @RequestParam(required = false) List<Genre> genres,
                               Model model) {
        contentService.updateGenres(contentId, genres != null ? genres : List.of());
        return "redirect:/content/" + contentId + "/genre";

    }
    //////////////////////////////////////////////////////////////////////////////////////////
}


//데이터 저장처리 (Service -> Controller
//ContentCreateDTO savedContent = contentService.create(contentDTO);
//        return "redirect:/content/" + savedContent.getId() + "/genre";