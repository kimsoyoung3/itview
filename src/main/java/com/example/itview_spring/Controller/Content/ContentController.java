package com.example.itview_spring.Controller.Content;

import com.example.itview_spring.DTO.*;
import com.example.itview_spring.Entity.ContentEntity;
import com.example.itview_spring.Service.ContentService;
import com.example.itview_spring.Util.PageInfo;
import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.NoSuchElementException;

@Controller
@RequiredArgsConstructor
public class ContentController {
    @Autowired
    private final ContentService contentService;  // 콘텐츠 서비스

    private final PageInfo pageInfo;
    @Autowired
    private ModelMapper modelMapper;

    // 등록 폼 이동
    @GetMapping("/content/register")
    public String newContent(Model model) {
        // 등록 모드: 빈 객체를 모델에 추가하여 템플릿에 전달
        model.addAttribute("contentDTO", new ContentCreateDTO());
        return "content/form";
    }

    // 등록 처리 후 → 장르 선택 페이지로 이동
    @PostMapping("/content/register")
    public String newContent(ContentCreateDTO contentDTO, RedirectAttributes redirectAttributes) {
        try {
            // 데이터 저장 및 S3 파일 업로드 처리
            ContentCreateDTO savedContent = contentService.create(contentDTO);

            // 성공 메시지 추가
            redirectAttributes.addFlashAttribute("message", "콘텐츠가 성공적으로 등록되었습니다.");


            // 장르 등록 폼으로 이동 (새로 생성된 ID를 사용)
            return "redirect:/content/" + savedContent.getId() + "/genre";
        } catch (IOException e) {
            // 파일 업로드 실패 시 예외 처리
            redirectAttributes.addFlashAttribute("errorMessage", "콘텐츠 등록 중 파일 업로드에 실패했습니다. 다시 시도해주세요.");
            e.printStackTrace(); // 로깅

            // 등록 폼으로 다시 이동
            return "redirect:/content/register";
        }
    }

    // 전체 조회
    @GetMapping("/content/list")
    public String listContent(
            @PageableDefault(page = 0) Pageable pageable,
            @RequestParam(required = false) String keyword,
            Model model) {

        Page<ContentCreateDTO> contentDTOS = contentService.getAllContents(keyword, null, pageable);
        model.addAttribute("contentDTOS", contentDTOS);

        int currentBlock = contentDTOS.getNumber() / 10;
        int startPage = currentBlock * 10;
        int endPage = Math.min(startPage + 9, contentDTOS.getTotalPages() - 1);
        if (contentDTOS.getTotalPages() == 0) {
            endPage = 0;
        }

        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        model.addAttribute("keyword", keyword); // 키워드를 모델에 추가하여 검색 후에도 유지되도록 함

        return "content/list";
    }

    // 상세 보기 (id 파라미터로 받음, @PathVariable 대신 @RequestParam 형식)
    @GetMapping("/content/detail")
    public String detailContent(@RequestParam Integer id, Model model) {
        try {
            AdminContentDTO contentDTO = contentService.read(id);
            model.addAttribute("contentDTO", contentDTO);
            return "content/detail"; // Renders content detail page
        } catch (NoSuchElementException e) {
            model.addAttribute("error", "해당 콘텐츠를 찾을 수 없습니다.");
            return "error/404"; // 또는 에러 페이지로 리디렉션
        }
    }

    // 수정 폼 이동
    @GetMapping("/content/{id}/update")
    public String updateContentForm(@PathVariable("id") Integer id, Model model) {
        try {
            // 1. 서비스의 findById 메소드를 사용해 엔티티를 직접 가져옵니다.
            ContentEntity contentEntity = contentService.findById(id);

            // 2. 엔티티를 DTO로 변환합니다.
            ContentCreateDTO contentDTO = modelMapper.map(contentEntity, ContentCreateDTO.class);

            // 3. 포스터 URL을 모델에 추가합니다.
            model.addAttribute("posterUrl", contentEntity.getPoster());

            // 4. 공개일(LocalDate)을 "yyyy-MM-dd" 형식의 문자열로 변환하여 모델에 추가합니다.
            // 이 코드가 날짜 형식 오류를 해결하는 핵심입니다.
            if (contentEntity.getReleaseDate() != null) {
                String releaseDateStr = contentEntity.getReleaseDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                model.addAttribute("releaseDateStr", releaseDateStr);
            }

            // 5. DTO를 모델에 추가합니다.
            model.addAttribute("contentDTO", contentDTO);

            return "content/form";
        } catch (NoSuchElementException e) {
            // 콘텐츠를 찾지 못했을 때 오류 처리
            return "redirect:/content/list";
        }
    }

    // 수정 처리 (→ 장르 수정 화면으로 리디렉트) //0909 수정처리함
    @PostMapping("/content/{id}/update")
    public String updateContentProc(@PathVariable("id") Integer id, ContentCreateDTO contentDTO, RedirectAttributes redirectAttributes) {
        try {
            // 콘텐츠 수정 처리
            ContentCreateDTO savedContent = contentService.update(id, contentDTO);

            // 수정 완료 메시지 추가
            redirectAttributes.addFlashAttribute("message", "콘텐츠가 성공적으로 수정되었습니다.");

            // 장르 수정 페이지로 리디렉션
            return "redirect:/content/" + savedContent.getId() + "/genre";
        } catch (NoSuchElementException e) {
            // 콘텐츠 ID가 유효하지 않을 경우
            redirectAttributes.addFlashAttribute("error", "콘텐츠를 찾을 수 없어 수정할 수 없습니다.");
            return "redirect:/content/list"; // 목록 페이지로 리다이렉트
        } catch (IOException e) {
            // 파일 업로드 실패 등 입출력 오류 발생 시
            redirectAttributes.addFlashAttribute("error", "콘텐츠 수정 중 파일 업로드에 실패했습니다. 다시 시도해주세요.");
            e.printStackTrace(); // 콘솔에 오류 로그 출력
            return "redirect:/content/" + id + "/update"; // 수정 폼으로 다시 리다이렉트
        }
    }

    // 삭제 처리 (id를 @RequestParam 으로 받음)
    @GetMapping("/content/delete")
    public String deleteContent(@RequestParam("id") Integer id) {
        contentService.delete(id);
        return "redirect:/content/list";
    }
}