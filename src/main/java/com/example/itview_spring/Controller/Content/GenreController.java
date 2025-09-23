package com.example.itview_spring.Controller.Content;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.example.itview_spring.Util.PageInfo;
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
    //콘텐츠 장르 등록/수정 페이지 {/content/{id}/genre)
    // 장르 선택/수정 폼
    @GetMapping("/content/{contentId}/genre")
    public String showGenreForm(@PathVariable Integer contentId, Model model) {

        // 1. 해당 콘텐츠 ID로 콘텐츠 정보 조회
        ContentCreateDTO contentDTO = genreService.read(contentId);

        // 2. 콘텐츠에 이미 등록된 장르 조회 (Genre Enum 리스트)
        List<Genre> selectedGenres = genreService.getGenresByContentId(contentId); // 이 메서드가 Enum List 반환
        List<String> selectedGenreNames = selectedGenres.stream()
                .map(Enum::name)
                .toList(); // ⚠️ 타입: List<Genre>// ✅ 템플릿에서 체크하기 좋게

        // 3. 장르 번역 Map (Enum 기반)
        Map<String, String> genreTranslations = Arrays.stream(Genre.values())
                .collect(Collectors.toMap(Enum::name, Genre::getGenreName));

        // 4. 도델에 전달할 데이터 구성
        // 해당 contentId로 ContentDTO, 모든 장르 리스트, 선택된 장르 리스트, 장르 번역 등을 모델에 추가
        model.addAttribute("contentDTO", contentDTO);                  // 콘텐츠 정보
        model.addAttribute("contentId", contentId);                    // ✅ Video 등록 시 활용 가능
        model.addAttribute("allGenres", Genre.values());               // 전체 장르 목록
        model.addAttribute("selectedGenres", selectedGenreNames);      // 선택된 장르 문자열
        model.addAttribute("genreTranslations", genreTranslations);    // 한글 번역

        return "content/genreForm"; // 장르 선택 HTML

    }
    //********************************************************************************//

    //********************************************************************************//
    // 장르 저장
    @PostMapping("/content/{contentId}/genre")
    public String submitGenres(@PathVariable Integer contentId,
                               @RequestParam(value = "genres", required = false) List<String> genreNames) {

        List<Genre> genres = new ArrayList<>();
        if (genreNames != null) {
            for (String name : genreNames) {
                try {
                    genres.add(Genre.valueOf(name));
                } catch (IllegalArgumentException e) {
                    System.err.println("Unknown Genre: " + name);
                }
            }
        }

        // 기존 장르 삭제 후 새로 추가
        genreService.updateGenres(contentId, genres);

        // 장르 저장 후 크레딧 등록 페이지로 리다이렉트
        return "redirect:/content/" + contentId + "/credit";
    }

}
