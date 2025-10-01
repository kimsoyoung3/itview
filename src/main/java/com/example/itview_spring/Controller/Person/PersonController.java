// src/main/java/com/example/itview_spring/Controller/Person/PersonController.java
package com.example.itview_spring.Controller.Person;

import com.example.itview_spring.DTO.PersonDTO;
import com.example.itview_spring.Entity.PersonEntity;
import com.example.itview_spring.Service.PersonService;
import com.example.itview_spring.Util.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;
    private final S3Uploader s3Uploader;

    /* ======= 매핑 유틸 ======= */
    private PersonDTO toDTO(PersonEntity e) {
        if (e == null) return null;
        PersonDTO dto = new PersonDTO();
        dto.setId(e.getId());
        dto.setName(e.getName());
        dto.setJob(e.getJob());
        dto.setProfile(e.getProfile());
        return dto;
    }
    private PersonEntity toEntity(PersonDTO d) {
        if (d == null) return null;
        PersonEntity e = new PersonEntity();
        e.setId(d.getId());
        e.setName(d.getName());
        e.setJob(d.getJob());
        e.setProfile(d.getProfile());
        return e;
    }

    /* ======= 목록 ======= */
    @GetMapping("/person/list")
    public String list(@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                       @RequestParam(required = false) String keyword,
                       Model model) {

        // 1. 통합된 서비스 메서드 호출: 키워드 유무에 따라 검색 또는 전체 목록 조회
        // (PersonService.getAllPeople는 keyword가 null일 때 findAll을 호출하도록 수정됨)
        Page<PersonDTO> personDTOS = personService.list(keyword, pageable);

        // 2. 모델에 데이터 추가
        model.addAttribute("page", personDTOS);
        model.addAttribute("personList", personDTOS.getContent());

        // 3. 페이지 블록 계산 (Thymeleaf 'null' 에러 방지)
        int currentBlock = personDTOS.getNumber() / 10; // 현재 페이지 블록 인덱스 (0, 1, 2...)
        int startPage = currentBlock * 10; // 0, 10, 20... (0-기반 인덱스)
        int endPage = Math.min(startPage + 9, personDTOS.getTotalPages() - 1);

        // 전체 페이지가 0일 때의 안전장치
        if (personDTOS.getTotalPages() == 0) {
            startPage = 0;
            endPage = 0;
        }

        // 4. 모델에 페이징 및 검색 정보 추가
        model.addAttribute("startPage", startPage); // ⬅️ Thymeleaf 오류 해결!
        model.addAttribute("endPage", endPage);
        model.addAttribute("keyword", keyword); // ⬅️ 검색어 유지
        model.addAttribute("baseUrl", "/person/list"); // ⬅️ 페이징 링크의 기본 URL

        return "Person/list";
    }

    /* ======= 상세 ======= */
    @GetMapping("/person/{id}")
    public String detail(@PathVariable Integer id, Model model) {
        var entity = personService.get(id);
        model.addAttribute("personDTO", toDTO(entity));
        return "Person/detail";
    }

    /* ======= 등록 폼 ======= */
    @GetMapping("/person/register")
    public String registerForm(Model model) {
        model.addAttribute("personDTO", new PersonDTO());
        return "Person/form";
    }

    /* ======= 등록 저장 ======= */
    @PostMapping("/person/register")
    public String register(@ModelAttribute("personDTO") PersonDTO personDTO) {
        personService.create(toEntity(personDTO));
        return "redirect:/person/list";
    }

    /* ======= 수정 폼 ======= */
    @GetMapping("/person/{id}/update")
    public String updateForm(@PathVariable Integer id, Model model) {
        var entity = personService.get(id);
        model.addAttribute("personDTO", toDTO(entity));
        return "Person/form";
    }

    /* ======= 수정 저장 (폼은 POST + _method=PUT) ======= */
    @PutMapping("/person/{id}/update")
    public String update(@PathVariable("id") Integer id,
                         @ModelAttribute("personDTO") PersonDTO personDTO) {
        personDTO.setId(id);

        // 기존 인물 정보 조회 (기존 파일 삭제를 위해 필요)
        PersonEntity existingPerson = personService.get(id);

        // 1. S3 URL 변경 여부 확인 (AJAX로 이미 profile 필드가 업데이트됨)
        String newProfileUrl = personDTO.getProfile();
        String oldProfileUrl = existingPerson.getProfile();

        // 2. 만약 새 URL이 있고 기존 URL과 다르다면, 기존 S3 파일 삭제
        //    (프로필 이미지를 변경했거나 새로 추가한 경우)
        if (StringUtils.hasText(newProfileUrl) && !newProfileUrl.equals(oldProfileUrl)) {
            if (StringUtils.hasText(oldProfileUrl)) {
                // 기존 S3 파일 삭제
                s3Uploader.deleteFile(oldProfileUrl);
            }
        }
        // 3. 만약 DTO의 profile이 null/empty인데 기존 URL이 있다면 (이미지 삭제 요청 시), 삭제.
        else if (!StringUtils.hasText(newProfileUrl) && StringUtils.hasText(oldProfileUrl)) {
            s3Uploader.deleteFile(oldProfileUrl);
            personDTO.setProfile(null); // DB에도 null 저장
        }
        // 4. 새 URL이 없고 기존 URL이 있다면, DTO에 기존 URL을 설정 (이미지 변경 안 한 경우)
        else if (!StringUtils.hasText(newProfileUrl) && StringUtils.hasText(oldProfileUrl)) {
            personDTO.setProfile(oldProfileUrl);
        }

        personService.update(id, toEntity(personDTO));
        return "redirect:/person/list";
    }

    /* ======= 삭제 (폼은 POST + _method=DELETE) ======= */
    @DeleteMapping("/person/{id}")
    public String delete(@PathVariable Integer id) {
        // DB에서 인물 정보를 가져와 프로필 이미지 URL을 얻습니다.
        PersonEntity existingPerson = personService.get(id);

        // 프로필 이미지가 존재하면 S3에서 먼저 삭제합니다.
        if (existingPerson != null && existingPerson.getProfile() != null && !existingPerson.getProfile().isEmpty()) {
            s3Uploader.deleteFile(existingPerson.getProfile());
        }

        // 데이터베이스에서 인물 정보를 삭제합니다.
        personService.delete(id);

        return "redirect:/person/list";
    }

    @PostMapping("/api/upload/profile")
    @ResponseBody
    public ResponseEntity<Map<String, String>> uploadProfileImage(
            @RequestParam("file") MultipartFile file) {

        // 1. 파일이 비어있는지 검사
        if (file.isEmpty()) {
            return new ResponseEntity<>(
                    // 클라이언트에게 오류 메시지 전달
                    Map.of("message", "파일이 비어있습니다. 파일을 선택해주세요."),
                    HttpStatus.BAD_REQUEST // 400
            );
        }

        try {
            // 2. S3Uploader 호출 및 URL 획득
            // S3Uploader 내부에서 Thumbnails 라이브러리가 지원하지 않는 포맷을 받으면
            // IOException이나 IllegalArgumentException이 발생할 수 있습니다.
            String s3Url = s3Uploader.uploadFile(file);

            // 3. 성공 시 URL 반환 (HTTP 200 OK)
            return new ResponseEntity<>(
                    Map.of("url", s3Url),
                    HttpStatus.OK
            );

        } catch (IllegalArgumentException e) {
            // S3Uploader 내부에서 발생한 파일 형식 관련 오류(예: 'image/webp'를 제대로 처리 못함)
            return new ResponseEntity<>(
                    Map.of("message", "파일 처리 중 오류가 발생했습니다. JPG, PNG, GIF 파일인지 확인해주세요."),
                    HttpStatus.BAD_REQUEST // 400
            );
        } catch (IOException e) {
            // S3 통신 오류 또는 Thumbnails IO 오류 등 처리
            return new ResponseEntity<>(
                    Map.of("message", "파일 업로드 중 서버 통신 오류가 발생했습니다."),
                    HttpStatus.INTERNAL_SERVER_ERROR // 500
            );
        }
    }
}