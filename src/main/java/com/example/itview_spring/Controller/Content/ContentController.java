package com.example.itview_spring.Controller.Content;

import com.example.itview_spring.Constant.Genre;
import com.example.itview_spring.DTO.ContentCreateDTO;
import com.example.itview_spring.DTO.ContentDetailDTO;
import com.example.itview_spring.DTO.PageInfoDTO;
import com.example.itview_spring.DTO.VideoDTO;
import com.example.itview_spring.Service.ContentService;
import com.example.itview_spring.Service.VideoService;
import com.example.itview_spring.Util.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Controller
@RequiredArgsConstructor
public class ContentController {
    private final VideoService videoService;
    private final ContentService contentService;
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

        // 1. 해당 콘텐츠 ID로 콘텐츠 정보 조회
        ContentCreateDTO contentDTO = contentService.read(contentId);
        // 2. 콘텐츠에 이미 등록된 장르 조회 (Genre Enum 리스트)
        List<Genre> selectedGenres = contentService.getGenresByContentId(contentId); // 이 메서드가 Enum List 반환

        // 3. Enum → 문자열 리스트로 변환 (ex: ["ACTION", "DRAMA"])
        List<String> selectedGenreNames = selectedGenres.stream()
                .map(Enum::name)
                .toList(); // ⚠️ 타입: List<Genre>// ✅ 템플릿에서 체크하기 좋게
        model.addAttribute("selectedGenres", selectedGenreNames); // ✅ 수정: 항상 String 리스트로
        // 중복된 addAttribute 제거

//        model.addAttribute("selectedGenres", genreService.getSelectedGenreNames(contentId));  // 이미 선택된 경우 표시
//        model.addAttribute("genreTranslations", genreService.getGenreTranslations()); // 한글 이름 맵핑

        // 4. 장르 번역 Map (영문 → 한글)
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
        // 5. 뷰에 전달할 데이터 구성
        // 해당 contentId로 ContentDTO, 모든 장르 리스트, 선택된 장르 리스트, 장르 번역 등을 모델에 추가
        // 모델에 필요한 데이터 전달
        // 5. 뷰에 전달할 데이터 구성
        model.addAttribute("contentDTO", contentDTO);                  // 콘텐츠 정보
        model.addAttribute("contentId", contentId);                    // ✅ Video 등록 시 활용 가능
        model.addAttribute("allGenres", Genre.values());               // 전체 장르 목록
        model.addAttribute("selectedGenres", selectedGenreNames);      // 선택된 장르 문자열
        model.addAttribute("genreTranslations", genreTranslations);    // 한글 번역


        // 6. 로그 출력 (개발 중 확인용)
        System.out.println(" get33 contentId ==" + contentId);
        System.out.println(" get33 contentDTO ==" + contentDTO);
        System.out.println(" get33 allGenres ==" + allGenres);
        System.out.println(" get33 selectedGenres ==" + selectedGenres);
        System.out.println(" get33 genreTranslations ==" + genreTranslations);
        System.out.println(" ---------------------");
        // 7. 최종 뷰 페이지로 이동
        return "content/genreForm"; // 장르 선택 HTML

    }
    //********************************************************************************//

    //********************************************************************************//
    // 장르 저장
    @PostMapping("/content/{contentId}/genre")
    public String saveContentGenres(
            @PathVariable Long contentId,
            @RequestParam(value = "genres", required = false) List<String> genreNames,
            RedirectAttributes redirectAttributes) {

        try {
            contentService.saveContentGenres(contentId, genreNames);
            redirectAttributes.addFlashAttribute("message", "장르가 성공적으로 저장되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "장르 저장 실패: " + e.getMessage());
        }

        return "redirect:/content/" + contentId + "/genre";
    }


    // 장르 저장
    @PostMapping("/content/{contentId}/genre")
    public String submitGenres(@PathVariable("contentId") Integer contentId,
                               @RequestParam(value = "genres", required = false) List<String> genreNames){


        System.out.println("✅ [장르 저장] contentId  == " + contentId);
        System.out.println("✅ [장르 저장] genreNames == " + genreNames);
        System.out.println(" ---------------------");

        // ✅ 문자열 → Genre 변환 시 오류 방지
        List<Genre> genres = new ArrayList<>();
        if (genreNames != null) {
            for (String name : genreNames) {
                try {
                    genres.add(Genre.valueOf(name));
                } catch (IllegalArgumentException e) {
                    System.err.println("⛔️ Unknown Genre: " + name); // 또는 로깅
                }
            }
        }

        // 장르 업데이트
        contentService.updateGenres(contentId, genres != null ? genres : List.of());

        // 업데이트된 내용 화면에 전달
        ContentCreateDTO contentDTO = contentService.read(contentId);
        List<Genre> selectedGenres = contentService.getGenresByContentId(contentId);
        List<String> selectedGenreNames = selectedGenres.stream()
                .map(Enum::name)
                .collect(Collectors.toList());
        Map<String, String> genreTranslations = Arrays.stream(Genre.values())
                .collect(Collectors.toMap(Enum::name, Genre::getGenreName));

        model.addAttribute("selectedGenres", selectedGenreNames);
        model.addAttribute("allGenres", Genre.values());
        model.addAttribute("genreTranslations", genreTranslations);// Map<String, String>
        // 모델에 필요한 데이터 다시 담기 (✅ 중요)
        // ✅ redirect 이후 model 사용하지 않으므로, addAttribute 생략

        Genre[] allGenres = Genre.values();
        // 장르 번역 맵 (10개 초과이므로 Map.ofEntries 사용)
        // redirect: 후 model.addAttribute()는 무의미함
//        Map<String, String> genreTranslations = Map.ofEntries(
//                Map.entry("ACTION", "액션"),
//                Map.entry("DRAMA", "드라마"),
//                Map.entry("ROMANCE", "로맨스"),
//                Map.entry("HORROR", "호러"),
//                Map.entry("THRILLER", "스릴러"),
//                Map.entry("COMEDY", "코미디"),
//                Map.entry("FANTASY", "판타지"),
//                Map.entry("ADVENTURE", "어드벤처"),
//                Map.entry("NATURAL_SCIENCE", "자연과학"),
//                Map.entry("KPOP", "K-POP"),
//                Map.entry("ANIMATION", "애니메이션"),
//                Map.entry("SPORTS", "스포츠"),
//                Map.entry("MYSTERY", "미스터리"),
//                Map.entry("DOCUMENTARY", "다큐멘터리"),
//                Map.entry("SF", "SF"),
//                Map.entry("MUSIC", "음악"),
//                Map.entry("FAMILY", "가족"),
//                Map.entry("CONCERT", "공연실황"),
//                Map.entry("MUSICAL", "뮤지컬"),
//                Map.entry("BIOPIC", "전기"),
//                Map.entry("HISTORY", "역사"),
//                Map.entry("CRIME", "범죄"),
//                Map.entry("KIDS", "키즈"),
//                Map.entry("VARIETY", "예능"),
//                Map.entry("SITCOM", "시트콤"),
//                Map.entry("PERIOD", "시대극"),
//                Map.entry("ROMANTIC_COMEDY", "로맨틱 코미디"),
//                Map.entry("BL", "BL"),
//                Map.entry("TEENAGER", "틴에이저"),
//                Map.entry("DISASTER", "재난"),
//                Map.entry("COMICS", "만화"),
//                Map.entry("HUMANITIES", "인문학"),
//                Map.entry("ECONOMICS", "경제서"),
//                Map.entry("INVESTMENT", "투자서"),
//                Map.entry("NOVEL", "소설"),
//                Map.entry("ESSAY", "에세이"),
//                Map.entry("SELF_HELP", "자기계발"),
//                Map.entry("WAR", "전쟁"),
//                Map.entry("PLAY", "희곡"),
//                Map.entry("POETRY", "시"),
//                Map.entry("SLICE_OF_LIFE", "일상"),
//                Map.entry("HIP_HOP", "힙합"),
//                Map.entry("POP", "팝"),
//                Map.entry("MOVIE_SOUNDTRACK", "영화음악"),
//                Map.entry("TV_SOUNDTRACK", "드라마음악"),
//                Map.entry("BALLAD", "발라드"),
//                Map.entry("DANCE", "댄스"),
//                Map.entry("ROCK", "록"),
//                Map.entry("CLASSICAL", "클래식"),
//                Map.entry("INDIE", "인디"),
//                Map.entry("ELECTRONICA", "일렉트로니카"),
//                Map.entry("JPOP", "JPOP"),
//                Map.entry("RNB", "알앤비"),
//                Map.entry("TROT", "트로트")
//        );
        //0825 영상등록 버튼 없을때 자료
          return "redirect:/content/" + contentId + "/genre";
        //  return "redirect:/content/" + contentId + "/video";


        // ✅ 장르 저장 후 video 등록 화면으로 이동 (GET 방식)
        // return "redirect:/video/register?contentId=" + contentId;

    }

/////////0825 vidio 추가///////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 영상 등록/수정 폼
     */
    @GetMapping("/content/{contentId}/video")
    public String showVideoForm(@PathVariable Integer contentId, Model model) {
        // 콘텐츠 정보
        ContentCreateDTO contentDTO = contentService.read(contentId);
        model.addAttribute("contentDTO", contentDTO);
        model.addAttribute("contentId", contentId);

        // 기존 영상 데이터가 있다면 조회
        VideoDTO videoDTO = contentService.getVideoByContentId(contentId);// <- 존재 여부 체크

        if (videoDTO == null) {
            videoDTO = new VideoDTO(); // 새로 생성
        }
        model.addAttribute("videoDTO", videoDTO); // 항상 전달

        return "content/videoForm"; // 템플릿 경로: templates/content/videoForm.html
    }

    /**
     * 영상 등록 또는 수정 처리
     */
    @PostMapping("/content/{contentId}/video")
    public String submitVideo(@PathVariable Integer contentId,
                              @ModelAttribute VideoDTO videoDTO) {

        System.out.println("✅ [Video 저장] contentId == " + contentId);
        System.out.println("✅ [Video 저장] videoDTO == " + videoDTO);

        // contentId 연결
        videoDTO.setContentId(contentId);

        // 등록 또는 수정
        if (videoDTO != null && videoDTO.getId() == null) {
            contentService.createVideo(contentId,videoDTO); // 신규 등록
        } else if (videoDTO != null) {
            contentService.updateVideo(contentId,videoDTO);// 기존 수정
        }

        // ✅ 저장 후 콘텐츠 상세페이지 혹은 다음 등록 화면으로 이동
        return "redirect:/content/" + contentId + "/video"; // 예: 요약 페이지
    }

    /**
     * 영상 삭제 처리
     */
    @PostMapping("/content/{contentId}/video/delete")
    public String deleteVideo(@PathVariable Integer contentId) {
        System.out.println("🗑️ [Video 삭제] contentId == " + contentId);

        contentService.deleteVideo(contentId);

        // 삭제 후 영상 등록 페이지로 리다이렉트
        return "redirect:/content/" + contentId + "/video";
    }

/////////0825 vidio 추가///////////////////////////////////////////////////////////////////////////////////////////








}
