package com.example.itview_spring.Controller.Content;

import aj.org.objectweb.asm.commons.Remapper;
import com.example.itview_spring.Constant.Genre;
import com.example.itview_spring.DTO.*;
import com.example.itview_spring.Entity.PersonEntity;
import com.example.itview_spring.Entity.VideoEntity;
import com.example.itview_spring.Repository.VideoRepository;
import com.example.itview_spring.Service.ContentService;
//import com.example.itview_spring.Service.VideoService;
import com.example.itview_spring.Util.PageInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Controller
@RequiredArgsConstructor
public class ContentController {
    //  private final VideoService videoService;      //Video 서비스
    //  private final GalleryService galleryService;  // 갤러리 서비스
    @Autowired
    private final ContentService contentService;  // 콘텐츠 서비스
    private final VideoRepository videoRepository;
    private final ModelMapper modelMapper;

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
    //********************************************************************************//

    //********************************************************************************//
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
        contentService.updateGenres(contentId, genres != null ? genres : List.of());

        // 업데이트된 내용 화면에 전달
        ContentCreateDTO contentDTO = contentService.read(contentId);
        List<Genre> selectedGenres = contentService.getGenresByContentId(contentId);
        List<String> selectedGenreNames = selectedGenres.stream()
                .map(Enum::name)
                .collect(Collectors.toList());
        Map<String, String> genreTranslations = Arrays.stream(Genre.values())
                .collect(Collectors.toMap(Enum::name, Genre::getGenreName));

//        model.addAttribute("selectedGenres", selectedGenreNames);
//        model.addAttribute("allGenres", Genre.values());
//        model.addAttribute("genreTranslations", genreTranslations);// Map<String, String>
        // 모델에 필요한 데이터 다시 담기 (✅ 중요)
        // ✅ redirect 이후 model 사용하지 않으므로, addAttribute 생략

        Genre[] allGenres = Genre.values();

        //0825 영상등록 버튼 없을때 자료
        return "redirect:/content/" + contentId + "/credit";
//          return "redirect:/content/" + contentId + "/genre";
        //  return "redirect:/content/" + contentId + "/video";


        // ✅ 장르 저장 후 video 등록 화면으로 이동 (GET 방식)
        // return "redirect:/video/register?contentId=" + contentId;

    }

/////////0901 credit 추가///////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 크레딧 등록/수정 폼(get)
     */
    @GetMapping("/content/{contentId}/credit")
    public String creditForm(@PathVariable("contentId") Integer contentId,
                             @RequestParam(value = "id", required = false) Integer creditId,
                             Model model) {

        // 1️⃣ 단일 CreditDTO 조회 (수정 모드)
        CreditDTO creditDTO;
        if (creditId != null) {
            // 수정 모드: 기존 크레딧 조회
            creditDTO = contentService.getCreditById(creditId);
            System.out.println("📌 contentId: " + contentId);
            System.out.println("📌 creditDTO.id: " + creditDTO.getId());

            if (creditDTO.getPerson() == null) {
                creditDTO.setPerson(new PersonDTO()); // 안전하게 PersonDTO 초기화
                System.out.println("📌 person.id: " + creditDTO.getPerson().getId());
                System.out.println("📌 person.name: " + creditDTO.getPerson().getName());
            }
        } else {
            System.out.println("⚠️ person 정보 없음");
            // 신규 등록 모드: 빈 CreditDTO + PersonDTO 포함
            creditDTO = new CreditDTO();
            creditDTO.setPerson(new PersonDTO());
        }
        model.addAttribute("creditDTO", creditDTO);

        // 2️⃣ 전체 CreditDTO 리스트 조회 (목록)
        List<CreditDTO> creditList = contentService.getCreditsByContentId(contentId);
        model.addAttribute("creditList", creditList);

        // 3️⃣ ContentId도 모델에 전달
        model.addAttribute("contentId", contentId);

        return "content/creditForm"; // 템플릿 경로
    }

    /**
     * 크레딧 등록 또는 수정 처리 (post)
     */
    @PostMapping("/content/{contentId}/credit")
    public String createOrUpdateCredits(
            @PathVariable("contentId") Integer contentId,
            @ModelAttribute CreditDTO creditDTO,
            RedirectAttributes redirectAttributes) {

        // Person 정보가 없으면 이름 기준으로 조회 후 없으면 생성
        if ((creditDTO.getPerson() == null || creditDTO.getPerson().getId() == null)
                && creditDTO.getPerson() != null && creditDTO.getPerson().getName() != null) {
            PersonEntity person = contentService.getOrCreatePersonByName(creditDTO.getPerson().getName());
            creditDTO.getPerson().setId(person.getId());
        }

        if (creditDTO.getId() == null) {
            // 신규 등록
            contentService.addCredits(contentId, List.of(creditDTO));
            redirectAttributes.addFlashAttribute("message", "크레딧이 등록되었습니다.");
        } else {
            // 수정 처리
            contentService.updateCredit(contentId, List.of(creditDTO));
            redirectAttributes.addFlashAttribute("message", "크레딧이 수정되었습니다.");
        }

        return "redirect:/content/" + contentId + "/credit";
    }

    /**
     * 크레딧 삭제 처리
     */
    @PostMapping("/content/{contentId}/credit/delete")
    public String deleteCredit(@PathVariable Integer contentId,
                               @RequestParam("creditId") Integer creditId,
                               RedirectAttributes redirectAttributes) {

        System.out.println("🗑️ [Credit 삭제] contentId == " + contentId);
        System.out.println("🗑️ [Credit 삭제] creditId == " + creditId);

        contentService.deleteCredit(creditId);

        redirectAttributes.addFlashAttribute("message", "크레딧이 삭제되었습니다.");

        return "redirect:/content/" + contentId + "/credit";
    }

/////////0901  gallery 추가///////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
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
                ? contentService.getGalleryById(galleryId)  // 갤러리 수정 모드일 경우 해당 갤러리 데이터를 조회
                : new GalleryDTO();                        // 신규 갤러리 등록 모드일 경우 새로운 객체 생성

        model.addAttribute("galleryDTO", galleryDTO);  // 모델에 갤러리 정보를 전달 (수정 또는 새로 추가된 갤러리 정보)

        // 2️⃣ 전체 GalleryDTO 리스트 조회 (목록)
        // contentId에 해당하는 모든 갤러리 목록을 조회
        List<GalleryDTO> galleryList = contentService.getGallerysByContentId(contentId);
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
            contentService.addGallery(contentId, galleryDTO); // 신규 갤러리 등록 처리
            redirectAttributes.addFlashAttribute("message", "갤러리가 등록되었습니다."); // 등록 성공 메시지
        } else {  // galleryDTO에 ID가 있으면 기존 갤러리 수정
            contentService.updateGallery(galleryDTO.getId(), (List<GalleryDTO>) galleryDTO); // 갤러리 수정 처리
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
        contentService.deleteGallery(galleryId);  // galleryId에 해당하는 갤러리 삭제 처리

        // 2️⃣ 삭제 후 성공 메시지 추가
        redirectAttributes.addFlashAttribute("message", "갤러리가 삭제되었습니다.");  // 삭제 완료 메시지

        // 삭제 후 갤러리 목록 페이지로 리다이렉트
        return "redirect:/content/" + contentId + "/gallery";  // 삭제 후 갤러리 목록 페이지로 리다이렉트
    }

/// //////gallery end //////////////////////////////////////////////////////////////////////////////////////////////


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////0825 vidio 추가///////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 영상 등록/수정 폼(get)
     */
    @GetMapping("/content/{contentId}/video")
    public String videoForm(@PathVariable("contentId") Integer contentId,
                            @RequestParam(value = "id", required = false) Integer videoId,
                            Model model) {

        // videoId로 VideoEntity 가져오기 (비즈니스 로직)

        VideoEntity videoEntity = videoRepository.findById(videoId)
                .orElseThrow(() -> new NoSuchElementException("해당 영상이 존재하지 않습니다: " + videoId));


        // 수정 후: Entity -> DTO 매핑 적용
        VideoDTO videoDTO = modelMapper.map(videoEntity, VideoDTO.class);
        model.addAttribute("videoDTO", videoDTO); // 모델에 VideoDTO 추가

        // 수정할 비디오가 없을 경우 기본값으로 초기화
        if (videoId != null && videoDTO == null) {
            throw new NoSuchElementException("해당 ID에 대한 영상이 존재하지 않습니다: " + videoId);  // 영상이 없으면 예외 처리
        }

        // 전체 VideoDTO 리스트 조회 (목록)
        List<VideoDTO> videoList = contentService.getVideosByContentId(contentId);  // `ContentService`에서 `getVideosByContentId` 메서드 호출로 변경
        model.addAttribute("videoList", videoList);  // 모델에 전체 영상 목록 추가

        // ContentId도 모델에 전달
        model.addAttribute("contentId", contentId); // 콘텐츠 ID를 모델에 전달

        return "content/videoForm";  // 영상 등록/수정 폼을 보여줄 템플릿 경로
    }

    /**
     * 영상 등록 또는 수정 처리 (post)
     */
    @PostMapping("/content/{contentId}/video")
    public String createVideo(
            @PathVariable("contentId") Integer contentId,
            @ModelAttribute VideoDTO videoDTO,
            RedirectAttributes redirectAttributes) {

        if (videoDTO.getId() == null) {
            // 신규 등록 처리
            contentService.createVideo(contentId, videoDTO);  // 신규 영상 등록
            redirectAttributes.addFlashAttribute("message", "등록되었습니다."); // 등록 완료 메시지
        } else {
            // 수정 처리
            contentService.updateVideo(videoDTO.getId(), videoDTO);  // 수정된 영상 처리
            redirectAttributes.addFlashAttribute("message", "수정되었습니다."); // 수정 완료 메시지
        }

        redirectAttributes.addAttribute("contentId", contentId);  // contentId를 리다이렉트에 포함
        return "redirect:/content/" + contentId + "/video";  // 다시 영상 리스트 페이지로 리다이렉트
    }

    /**
     * 영상 삭제 처리
     */
//삭제 처리 메서드에서는 contentService.deleteVideo(videoId)를 호출하여
// 해당 영상을 삭제하고, 삭제 완료 메시지를 리다이렉트 후 전달합니다.
    @PostMapping("/content/{contentId}/video/delete")
    public String deleteVideo(@PathVariable Integer contentId,
                              @RequestParam("videoId") Integer videoId,
                              RedirectAttributes redirectAttributes) {
        System.out.println("🗑️ [Video 삭제] contentId == " + contentId); // 삭제 로그 출력
        System.out.println("<UNK> [Video <UNK>] videoId == " + videoId);  // 삭제 로그 출력

        contentService.deleteVideo(videoId);  // 영상 삭제 메서드 호출
        redirectAttributes.addFlashAttribute("message", "삭제되었습니다.");  // 삭제 완료 메시지

        return "redirect:/content/" + contentId + "/video";  // 삭제 후 영상 리스트 페이지로 리다이렉트
    }

/// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// //////0828  ExternalService 추가///////////////////////////////////////////////////////////////////////////////////////////
/// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

@GetMapping("/content/{contentId}/external")
public String externalServiceForm(@PathVariable("contentId") Integer contentId,
                                  @RequestParam(value = "id", required = false) Integer externalServiceId,
                                  Model model) {
    // 1️⃣ 수정 모드 vs 등록 모드 구분
    ExternalServiceDTO externalServiceDTO = (externalServiceId != null)
            ? contentService.getExternalServiceById(externalServiceId)  // contentService에서 단일ExternalServiceId 조회 메서드 필요
            : new ExternalServiceDTO();                        // 등록 모드
    model.addAttribute("externalServiceDTO", externalServiceDTO);

    // 2️⃣ 전체ExternalServiceDTO 리스트 조회 (목록)
    List<ExternalServiceDTO> externalServiceList = contentService.getExternalServicesByContentId(contentId);
    model.addAttribute("externalServiceList", externalServiceList);

    // 3️⃣ ContentId도 모델에 전달
    model.addAttribute("contentId", contentId);

    return "content/externalForm"; // 템플릿 경로: templates/content/externalForm.html
}

/**
 * 외부서비스  등록 또는 수정 처리 (post)
 */
@PostMapping("/content/{contentId}/external")
public String createExternalService(
        @PathVariable("contentId") Integer contentId,
        @ModelAttribute("externalServiceDTO") @Valid ExternalServiceDTO externalServiceDTO,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes,
        Model model) {


    if (bindingResult.hasErrors()) {
        // 검증 실패 → 다시 폼으로
        model.addAttribute("externalServiceList", contentService.getExternalServicesByContentId(contentId));
        model.addAttribute("contentId", contentId);
        return "content/externalForm";  // 다시 입력폼 보여주기
    }

    if (externalServiceDTO.getId() == null) {
        // 신규 등록
        contentService.createExternalService(contentId, externalServiceDTO);
        redirectAttributes.addFlashAttribute("message", "등록되었습니다.");
    } else {
        // 수정 처리
        contentService.updateExternalService(externalServiceDTO.getId(), externalServiceDTO);
        redirectAttributes.addFlashAttribute("message", "수정되었습니다.");
    }

    return "redirect:/content/" + contentId + "/external";
}

/**
 * 외부서비스 삭제 처리
 */
@PostMapping("/content/{contentId}/external/delete")
public String deleteExternalService(@PathVariable Integer contentId,
                                    @RequestParam("externalServiceId") Integer externalServiceId,
                                    RedirectAttributes redirectAttributes) {
    System.out.println("🗑️ [ExternalService 삭제] contentId == " + contentId);
    System.out.println("<UNK> [ExternalService <UNK>] externalServiceId == " + externalServiceId);

    contentService.deleteExternalService(externalServiceId); //실제 externalServiceId 기반 삭제
    // ✅ 메시지 추가
    redirectAttributes.addFlashAttribute("message", "삭제되었습니다.");
    // 삭제 후 영상 등록 페이지로 리다이렉트

    return "redirect:/content/" + contentId + "/external";
}
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

}