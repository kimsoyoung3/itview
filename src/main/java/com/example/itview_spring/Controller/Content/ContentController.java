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
import java.util.Map;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

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
        ContentCreateDTO savedContent = contentService.create(contentDTO); // 저장 후

        System.out.println(" 00 savedContent =="+savedContent);
        return "redirect:/content/" + savedContent.getId() + "/genre";     // 장르 등록 폼으로 이동
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
    //    System.out.println("contentDTO.            ==", contentDTOS);

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
        //     System.out.println("contentDTO.getContentId==",contentDTO.getContentId());
        //     System.out.println("contentDTO.            ==",contentDTO);
        return "content/detail";
    }

    // 수정 폼
    @GetMapping("/content/{id}/update")
    public String updateContent(@PathVariable("id") Integer id, Model model) {
        ContentCreateDTO contentDTO = contentService.read(id);
        model.addAttribute("contentDTO", contentDTO);
        return "content/update";
    }

    // 수정 처리 (→ 장르 수정 화면으로 리디렉트)
    @PostMapping("/content/{id}/update")
    public String updateContentProc(@PathVariable("id") Integer id, ContentCreateDTO contentDTO) {
//        contentService.update(id, contentDTO);
//        return "redirect:/content/list";
        ContentCreateDTO savedContent = contentService.update(id, contentDTO);
        System.out.println(" 22 savedContent =="+savedContent)   ;
        return "redirect:/content/" + savedContent.getId() + "/genre";     // 장르 수정 폼으로 이동
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

    public String showGenreForm(@PathVariable Integer contentId, Model model) {
        ContentCreateDTO contentDTO = contentService.read(contentId);

        List<Genre> selectedGenres = contentService.getGenresByContentId(contentId); // 이 메서드가 Enum List 반환
        List<String> selectedGenreNames = selectedGenres.stream().map(Enum::name).toList(); // ✅ 변환

        model.addAttribute("contentId", contentId);
        model.addAttribute("allGenres", Genre.values());
//        model.addAttribute("selectedGenres", genreService.getSelectedGenreNames(contentId));  // 이미 선택된 경우 표시
//        model.addAttribute("genreTranslations", genreService.getGenreTranslations()); // 한글 이름 맵핑

        // 해당 contentId로 ContentDTO, 모든 장르 리스트, 선택된 장르 리스트, 장르 번역 등을 모델에 추가
        model.addAttribute("contentDTO", contentDTO);
        model.addAttribute("allGenres",Genre.values());
        model.addAttribute("selectedGenres", selectedGenres);

        Genre[] allGenres = Genre.values();
        Map<String, String> genreTranslations = Map.of(
                "ACTION", "액션",
                "DRAMA", "드라마",
                "ROMANCE", "로맨스",
                "HORROR", "호러",
                "THRILLER", "스릴러",
                "COMEDY", "코미디",
                "FANTASY", "판타지",
                "ADVENTURE", "어드벤처",
                "NATURAL_SCIENCE", "자연과학",
                "KPOP", "K-POP"
        );



        System.out.println(" get33 contentDTO =="+contentDTO);
        System.out.println(" get33 contentId =="+contentId);
        System.out.println(" get33 allGenres =="+allGenres);
        System.out.println(" get33 selectedGenres =="+selectedGenres);
        System.out.println(" get33 genreTranslations =="+genreTranslations);
        System.out.println(" ---------------------")   ;


        model.addAttribute("contentId", contentId);
        model.addAttribute("allGenres", allGenres);
        model.addAttribute("selectedGenres", selectedGenres);
        model.addAttribute("genreTranslations", genreTranslations);

        return "content/genreForm"; // 장르 선택 HTML
    }
 //********************************************************************************//
//    public String showGenreForm(@PathVariable("contentId") Integer contentId, Model model) {
////        model.addAttribute("contentId", id);
////        model.addAttribute("allGenres", Genre.values());
//
//        List<Genre> allGenres = Arrays.asList(Genre.values());
//
//        List<Genre> selectedGenres = contentService.getGenresByContentId(contentId);
//        List<String> selectedGenreNames = selectedGenres.stream()
//                .map(Enum::name)
//                .toList();
//
//        System.out.println(" get33 contentId ==",contentId)   ;
//        System.out.println(" get33 allgenres ==",allGenres)   ;
//        System.out.println(" get33 selectdGenreNames ==",selectedGenreNames)   ;
//        System.out.println(" ---------------------")   ;
//
//        Map<String, String> genreTranslations = Map.of(
//                "ACTION", "액션",
//                "DRAMA", "드라마",
//                "ROMANCE", "로맨스",
//                "HORROR", "호러",
//                "THRILLER", "스릴러",
//                "COMEDY", "코미디",
//                "FANTASY", "판타지",
//                "ADVENTURE", "어드벤처",
//                "NATURAL_SCIENCE", "자연과학",
//                "KPOP", "K-POP"
//        );
//        model.addAttribute("contentId", contentId); // ✅ 반드시 필요
//        model.addAttribute("allGenres", allGenres);
//        model.addAttribute("selectedGenres", selectedGenreNames);// ✅ 문자열만 전달
//        model.addAttribute("genreTranslations", genreTranslations);
//
//
//        return "content/genreForm";
//    }
    //********************************************************************************//
    // 장르 저장
    @PostMapping("/content/{contentId}/genre")
    public String submitGenres(@PathVariable("contentId") Integer contentId,
                               @RequestParam(value = "genres" ,required = false) List<Genre> genres,
                               Model model) {
        if (contentId == null) {
            contentService.addGenres(contentId, genres != null ? genres :
                    List.of());

            System.out.println(" post null contentId  =="+contentId);
            System.out.println(" post null genress    =="+genres);
            System.out.println(" ---------------------")   ;
        }else {

            System.out.println(" post44 contentId  =="+contentId);
            System.out.println(" post44 genress    =="+genres);
            System.out.println(" ---------------------")   ;
               contentService.updateGenres(contentId, genres != null ? genres :
                    List.of());
        }
        // 모델에 필요한 데이터 다시 담기 (✅ 중요)
        ContentCreateDTO contentDTO = contentService.read(contentId);
        List<Genre> selectedGenres = contentService.getGenresByContentId(contentId);
        List<String> selectedGenreNames = selectedGenres.stream().map(Enum::name).toList();

        Genre[] allGenres = Genre.values();
        Map<String, String> genreTranslations = Map.of(
                "ACTION", "액션",
                "DRAMA", "드라마",
                "ROMANCE", "로맨스",
                "HORROR", "호러",
                "THRILLER", "스릴러",
                "COMEDY", "코미디",
                "FANTASY", "판타지",
                "ADVENTURE", "어드벤처",
                "NATURAL_SCIENCE", "자연과학",
                "KPOP", "K-POP"
        );

        model.addAttribute("contentDTO", contentDTO);
        model.addAttribute("contentId", contentId);
        model.addAttribute("allGenres", allGenres);
        model.addAttribute("selectedGenres", selectedGenreNames);
        model.addAttribute("genreTranslations", genreTranslations);



//        model.addAttribute("selectedGenres", genres);
//        model.addAttribute("allGenres", Arrays.asList(Genre.values()));

        return "redirect:/content/" + contentId + "/genre";

    }
    //////////////////////////////////////////////////////////////////////////////////////////
}


//데이터 저장처리 (Service -> Controller
//ContentCreateDTO savedContent = contentService.create(contentDTO);
//        return "redirect:/content/" + savedContent.getId() + "/genre";