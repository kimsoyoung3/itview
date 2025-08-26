package com.example.itview_spring.Controller.User;

import com.example.itview_spring.Constant.Genre;
import com.example.itview_spring.DTO.ContentCreateDTO;
import com.example.itview_spring.DTO.PageInfoDTO;
import com.example.itview_spring.Service.ContentService;
import com.example.itview_spring.Util.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        // URL 경로 변수인 {id}를 받으려면 @PathVariable을 써야 합니다.
        ContentCreateDTO contentDTO = contentService.read(id);
        model.addAttribute("contentDTO", contentDTO);
        System.out.println("deteil id         ===>" + id);
        System.out.println("deteil contentDTO ===>" + contentDTO);
        return "content/detail"; // 경로가 정확한지 확인 필요
    }

//
//    @GetMapping("/content/{id:\\d+}")
//    public String detail(@RequestParam("id") Integer id, Model model) {
//        ContentDetailDTO detailDTO = contentService.getContentDetail(id);
//        model.addAttribute("contentDTO", detailDTO.getContentInfo()); // ContentResponseDTO
//        model.addAttribute("gallery", detailDTO.getGallery());
//        model.addAttribute("videos", detailDTO.getVideos());
//        model.addAttribute("externalServices", detailDTO.getExternalServices());
//        return "content/detail";
//    }

    // 수정 폼 이동
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
        // Service에서 수정 처리 후 저장된 DTO 반환 받음
        ContentCreateDTO savedContent = contentService.update(id, contentDTO);
//        System.out.println(" 22 savedContent ==" + savedContent);
        // 수정 후 바로 장르 수정 페이지로 이동
        return "redirect:/content/" + savedContent.getId() + "/genre";     // 장르 수정 폼으로 이동
    }

    // 삭제 처리 (id를 @RequestParam 으로 받음)
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
        // 선택된 장르 (List<Genre>) → List<String>으로 변환
        List<Genre> selectedGenres = contentService.getGenresByContentId(contentId); // 이 메서드가 Enum List 반환
        List<String> selectedGenreNames = selectedGenres.stream()
                .map(Enum::name)
                .toList(); // ⚠️ 타입: List<Genre>// ✅ 템플릿에서 체크하기 좋게
        model.addAttribute("selectedGenres", selectedGenreNames); // ✅ 수정: 항상 String 리스트로
        // 중복된 addAttribute 제거

//        model.addAttribute("selectedGenres", genreService.getSelectedGenreNames(contentId));  // 이미 선택된 경우 표시
//        model.addAttribute("genreTranslations", genreService.getGenreTranslations()); // 한글 이름 맵핑


        Genre[] allGenres = Genre.values();
        // 장르 번역 맵 (10개 초과이므로 Map.ofEntries 사용)
        Map<String, String> genreTranslations = Map.ofEntries(
                Map.entry("ACTION", "액션"),
                Map.entry("DRAMA", "드라마"),
                Map.entry("ROMANCE", "로맨스"),
                Map.entry("HORROR", "호러"),
                Map.entry("THRILLER", "스릴러"),
                Map.entry("COMEDY", "코미디"),
                Map.entry("FANTASY", "판타지"),
                Map.entry("ADVENTURE", "어드벤처"),
                Map.entry("NATURAL_SCIENCE", "자연과학"),
                Map.entry("KPOP", "K-POP"),
                Map.entry("ANIMATION", "애니메이션"),
                Map.entry("SPORTS", "스포츠"),
                Map.entry("MYSTERY", "미스터리"),
                Map.entry("DOCUMENTARY", "다큐멘터리"),
                Map.entry("SF", "SF"),
                Map.entry("MUSIC", "음악"),
                Map.entry("FAMILY", "가족"),
                Map.entry("CONCERT", "공연실황"),
                Map.entry("MUSICAL", "뮤지컬"),
                Map.entry("BIOPIC", "전기"),
                Map.entry("HISTORY", "역사"),
                Map.entry("CRIME", "범죄"),
                Map.entry("KIDS", "키즈"),
                Map.entry("VARIETY", "예능"),
                Map.entry("SITCOM", "시트콤"),
                Map.entry("PERIOD", "시대극"),
                Map.entry("ROMANTIC_COMEDY", "로맨틱 코미디"),
                Map.entry("BL", "BL"),
                Map.entry("TEENAGER", "틴에이저"),
                Map.entry("DISASTER", "재난"),
                Map.entry("COMICS", "만화"),
                Map.entry("HUMANITIES", "인문학"),
                Map.entry("ECONOMICS", "경제서"),
                Map.entry("INVESTMENT", "투자서"),
                Map.entry("NOVEL", "소설"),
                Map.entry("ESSAY", "에세이"),
                Map.entry("SELF_HELP", "자기계발"),
                Map.entry("WAR", "전쟁"),
                Map.entry("PLAY", "희곡"),
                Map.entry("POETRY", "시"),
                Map.entry("SLICE_OF_LIFE", "일상"),
                Map.entry("HIP_HOP", "힙합"),
                Map.entry("POP", "팝"),
                Map.entry("MOVIE_SOUNDTRACK", "영화음악"),
                Map.entry("TV_SOUNDTRACK", "드라마음악"),
                Map.entry("BALLAD", "발라드"),
                Map.entry("DANCE", "댄스"),
                Map.entry("ROCK", "록"),
                Map.entry("CLASSICAL", "클래식"),
                Map.entry("INDIE", "인디"),
                Map.entry("ELECTRONICA", "일렉트로니카"),
                Map.entry("JPOP", "JPOP"),
                Map.entry("RNB", "알앤비"),
                Map.entry("TROT", "트로트")
        );

        // 해당 contentId로 ContentDTO, 모든 장르 리스트, 선택된 장르 리스트, 장르 번역 등을 모델에 추가
        // 모델에 필요한 데이터 전달
        model.addAttribute("contentDTO", contentDTO);
        model.addAttribute("contentId", contentId);
        model.addAttribute("allGenres", Genre.values());
        model.addAttribute("selectedGenres", selectedGenreNames); // ✅ String 리스트로!
        model.addAttribute("genreTranslations", genreTranslations);


        System.out.println(" get33 contentDTO ==" + contentDTO);
        System.out.println(" get33 contentId ==" + contentId);
        System.out.println(" get33 allGenres ==" + allGenres);
        System.out.println(" get33 selectedGenres ==" + selectedGenres);
        System.out.println(" get33 genreTranslations ==" + genreTranslations);
        System.out.println(" ---------------------");

        return "content/genreForm"; // 장르 선택 HTML
    }
    //********************************************************************************//

    //********************************************************************************//
    // 장르 저장
    @PostMapping("/content/{contentId}/genre")
    public String submitGenres(@PathVariable("contentId") Integer contentId,
                               @RequestParam(value = "genres", required = false) List<Genre> genres,
                               Model model) {

        System.out.println(" post 44 contentId  ==" + contentId);
        System.out.println(" post 44 genress    ==" + genres);
        System.out.println(" ---------------------");
//        contentService.updateGenres(contentId, genres != null ? genres :
//                List.of());

        // 장르 업데이트 (null 허용, 빈 리스트 처리 포함)
        contentService.updateGenres(contentId, genres != null ? genres : List.of());

        // 업데이트된 내용 화면에 전달
        ContentCreateDTO contentDTO = contentService.read(contentId);
        List<Genre> selectedGenres = contentService.getGenresByContentId(contentId);
        List<String> selectedGenreNames = selectedGenres.stream()
                .map(Enum::name).collect(Collectors.toList());
        // 모델에 필요한 데이터 다시 담기 (✅ 중요)
        // ✅ redirect 이후 model 사용하지 않으므로, addAttribute 생략

        Genre[] allGenres = Genre.values();
        // 장르 번역 맵 (10개 초과이므로 Map.ofEntries 사용)
        Map<String, String> genreTranslations = Map.ofEntries(
                Map.entry("ACTION", "액션"),
                Map.entry("DRAMA", "드라마"),
                Map.entry("ROMANCE", "로맨스"),
                Map.entry("HORROR", "호러"),
                Map.entry("THRILLER", "스릴러"),
                Map.entry("COMEDY", "코미디"),
                Map.entry("FANTASY", "판타지"),
                Map.entry("ADVENTURE", "어드벤처"),
                Map.entry("NATURAL_SCIENCE", "자연과학"),
                Map.entry("KPOP", "K-POP"),
                Map.entry("ANIMATION", "애니메이션"),
                Map.entry("SPORTS", "스포츠"),
                Map.entry("MYSTERY", "미스터리"),
                Map.entry("DOCUMENTARY", "다큐멘터리"),
                Map.entry("SF", "SF"),
                Map.entry("MUSIC", "음악"),
                Map.entry("FAMILY", "가족"),
                Map.entry("CONCERT", "공연실황"),
                Map.entry("MUSICAL", "뮤지컬"),
                Map.entry("BIOPIC", "전기"),
                Map.entry("HISTORY", "역사"),
                Map.entry("CRIME", "범죄"),
                Map.entry("KIDS", "키즈"),
                Map.entry("VARIETY", "예능"),
                Map.entry("SITCOM", "시트콤"),
                Map.entry("PERIOD", "시대극"),
                Map.entry("ROMANTIC_COMEDY", "로맨틱 코미디"),
                Map.entry("BL", "BL"),
                Map.entry("TEENAGER", "틴에이저"),
                Map.entry("DISASTER", "재난"),
                Map.entry("COMICS", "만화"),
                Map.entry("HUMANITIES", "인문학"),
                Map.entry("ECONOMICS", "경제서"),
                Map.entry("INVESTMENT", "투자서"),
                Map.entry("NOVEL", "소설"),
                Map.entry("ESSAY", "에세이"),
                Map.entry("SELF_HELP", "자기계발"),
                Map.entry("WAR", "전쟁"),
                Map.entry("PLAY", "희곡"),
                Map.entry("POETRY", "시"),
                Map.entry("SLICE_OF_LIFE", "일상"),
                Map.entry("HIP_HOP", "힙합"),
                Map.entry("POP", "팝"),
                Map.entry("MOVIE_SOUNDTRACK", "영화음악"),
                Map.entry("TV_SOUNDTRACK", "드라마음악"),
                Map.entry("BALLAD", "발라드"),
                Map.entry("DANCE", "댄스"),
                Map.entry("ROCK", "록"),
                Map.entry("CLASSICAL", "클래식"),
                Map.entry("INDIE", "인디"),
                Map.entry("ELECTRONICA", "일렉트로니카"),
                Map.entry("JPOP", "JPOP"),
                Map.entry("RNB", "알앤비"),
                Map.entry("TROT", "트로트")
        );
        // redirect: 후 model.addAttribute()는 무의미함


        return "redirect:/content/" + contentId + "/genre";

    }

}
