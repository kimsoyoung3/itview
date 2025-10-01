package com.example.itview_spring.Controller.Content;

import com.example.itview_spring.DTO.*;
import com.example.itview_spring.Entity.ContentEntity;
import com.example.itview_spring.Service.ContentService;
import com.example.itview_spring.Util.S3Uploader;
import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.NoSuchElementException;

@Controller
@RequiredArgsConstructor
public class ContentController {
    @Autowired
    private final ContentService contentService;  // 콘텐츠 서비스

    @Autowired
    private ModelMapper modelMapper;

    private final S3Uploader s3Uploader;

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
        // 데이터 저장 및 S3 파일 업로드 처리
        ContentCreateDTO savedContent = contentService.create(contentDTO);

        // 성공 메시지 추가
        redirectAttributes.addFlashAttribute("message", "콘텐츠가 성공적으로 등록되었습니다.");


        // 장르 등록 폼으로 이동 (새로 생성된 ID를 사용)
        return "redirect:/content/" + savedContent.getId() + "/genre";
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
        }
    }

    // 삭제 처리 (id를 @RequestParam 으로 받음)
    @GetMapping("/content/delete")
    public String deleteContent(@RequestParam("id") Integer id) {
        contentService.delete(id);
        return "redirect:/content/list";
    }

    @PostMapping("/content/poster-upload")
    @ResponseBody
    public ResponseEntity<Map<String, String>> uploadPoster(
            @RequestParam("poster") MultipartFile file) {

        // 1. 파일이 비어있는지 검사 (400 BAD REQUEST)
        if (file.isEmpty()) {
            return new ResponseEntity<>(
                    Map.of("message", "포스터 파일을 선택해주세요."),
                    HttpStatus.BAD_REQUEST
            );
        }

        try {
            // 2. S3Uploader 호출 및 URL 획득 (제공해주신 S3Uploader의 uploadFile 메서드 사용)
            String s3Url = s3Uploader.uploadFile(file);

            // 3. 성공 시 URL 반환 (200 OK)
            return new ResponseEntity<>(
                    Map.of("url", s3Url), // 'url' 키 사용
                    HttpStatus.OK
            );

        } catch (IllegalArgumentException e) {
            // 4. 파일 형식 관련 오류 (400 BAD REQUEST)
            // S3Uploader 내부의 Thumbnails.of() 처리 중 발생할 수 있는 예외
            e.printStackTrace();
            return new ResponseEntity<>(
                    Map.of("message", "파일 처리 중 오류가 발생했습니다. 이미지 파일(JPG, PNG 등)인지 확인해주세요."),
                    HttpStatus.BAD_REQUEST
            );
        } catch (IOException e) {
            // 5. 서버 통신/IO 오류 (500 INTERNAL SERVER ERROR)
            // S3 통신 오류 또는 Thumbnails IO 오류 등
            e.printStackTrace();
            return new ResponseEntity<>(
                    Map.of("message", "포스터 업로드 중 서버 통신 오류가 발생했습니다."),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}