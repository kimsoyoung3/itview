package com.example.itview_spring.Controller.Content;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.itview_spring.Constant.Genre;
import com.example.itview_spring.DTO.ContentCreateDTO;
import com.example.itview_spring.Service.ContentService;
import com.example.itview_spring.Service.GenreService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class GenreController {

    private final ContentService contentService;
    private final GenreService genreService;
    
    //콘텐츠 장르 등록/수정 페이지 {/content/{id}/genre)
    // 장르 선택/수정 폼
    @GetMapping("/content/{contentId}/genre")
    public String showGenreForm(@PathVariable Integer contentId, Model model) {

        // 1. 해당 콘텐츠 ID로 콘텐츠 정보 조회
        ContentCreateDTO contentDTO = contentService.read(contentId);
        // 2. 콘텐츠에 이미 등록된 장르 조회 (Genre Enum 리스트)
        List<Genre> selectedGenres = genreService.getGenresByContentId(contentId); // 이 메서드가 Enum List 반환

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
                Map.entry("TV_DRAMA", "TV드라마"),
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

    // 장르 저장
//    @PostMapping("/content/{contentId}/genre")
//    public String saveContentGenres(
//            @PathVariable Integer contentId,
//            @RequestParam(value = "genres", required = false) List<String> genreNames,
//            RedirectAttributes redirectAttributes) {
//
//        try {
//            contentService.saveContentGenres(contentId, genreNames);
//            redirectAttributes.addFlashAttribute("message", "장르가 성공적으로 저장되었습니다.");
//        } catch (Exception e) {
//            redirectAttributes.addFlashAttribute("errorMessage", "장르 저장 실패: " + e.getMessage());
//        }
//
//        return "redirect:/content/" + contentId + "/genre";
//    }

    // 장르 저장
    @PostMapping("/content/{contentId}/genre")
    public String submitGenres(@PathVariable("contentId") Integer contentId,
                               @RequestParam(value = "genres", required = false) List<String> genreNames) {


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
        genreService.updateGenres(contentId, genres != null ? genres : List.of());


//        model.addAttribute("selectedGenres", selectedGenreNames);
//        model.addAttribute("allGenres", Genre.values());
//        model.addAttribute("genreTranslations", genreTranslations);// Map<String, String>
        // 모델에 필요한 데이터 다시 담기 (✅ 중요)
        // ✅ redirect 이후 model 사용하지 않으므로, addAttribute 생략

        //0825 영상등록 버튼 없을때 자료
        return "redirect:/content/" + contentId + "/credit";
//          return "redirect:/content/" + contentId + "/genre";
        //  return "redirect:/content/" + contentId + "/video";


        // ✅ 장르 저장 후 video 등록 화면으로 이동 (GET 방식)
        // return "redirect:/video/register?contentId=" + contentId;

    }
}
