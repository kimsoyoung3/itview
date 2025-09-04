// src/main/java/com/example/itview_spring/Controller/Person/PersonController.java
package com.example.itview_spring.Controller.Person;

import com.example.itview_spring.DTO.PersonDTO;
import com.example.itview_spring.Entity.PersonEntity;
import com.example.itview_spring.Service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/person")
public class PersonController {

    private final PersonService personService;

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
    @GetMapping("/list")
    public String list(@PageableDefault(size = 10) Pageable pageable,
                       @RequestParam(required = false) String keyword,
                       Model model) {

        // 요구사항: keyword 가 있으면 /search/content 로 redirect (컨텐츠 검색)
        if (StringUtils.hasText(keyword)) {
            String redirectUrl = "/search/content?keyword=" + keyword.trim()
                    + "&page=" + pageable.getPageNumber()
                    + "&size=" + pageable.getPageSize();
            return "redirect:" + redirectUrl;
        }

        Page<PersonDTO> page = personService.list(pageable, null).map(this::toDTO);
        model.addAttribute("page", page);
        model.addAttribute("personList", page.getContent());
        model.addAttribute("keyword", null);
        return "Person/list";
    }



    /* ======= 등록 폼 ======= */
    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("personDTO", new PersonDTO());
        return "Person/register";
    }

    /* ======= 등록 저장 ======= */
    @PostMapping("/register")
    public String register(@ModelAttribute("personDTO") PersonDTO personDTO) {
        personService.create(toEntity(personDTO));
        return "redirect:/person/list";
    }



    /* ======= 수정 저장 (폼은 POST + _method=PUT) ======= */
    @PutMapping("/{id}/update")
    public String update(@PathVariable Integer id,
                         @ModelAttribute("personDTO") PersonDTO personDTO) {
        personService.update(id, toEntity(personDTO));
        return "redirect:/person/list";
    }

    /* ======= 삭제 (폼은 POST + _method=DELETE) ======= */
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Integer id) {
        personService.delete(id);
        return "redirect:/person/list";
    }
// PersonController.java 에서 해당 메서드만 교체/추가

    @GetMapping("/{id}")
    public String detail(@PathVariable Integer id, Model model) {
        var entity = personService.get(id); // 없으면 IllegalArgumentException → 404페이지로 바꾸고 싶으면 @ControllerAdvice로 처리
        model.addAttribute("personDTO", toDTO(entity));

        // 좋아요 상태/카운트는 실패해도 화면은 떠야 하므로 기본값 보장
        boolean liked = false;
        long likeCount = 0L;
        try {
            Integer userId = 1; // 로그인 연동 전 임시
            var resp = personService.getPersonResponseDTO(userId, id);
            if (resp != null) {
                // resp의 getter 이름은 DTO에 맞춰주세요 (isLiked/getLiked 등)
                // 아래는 예시:
                liked = Boolean.TRUE.equals(resp.getLiked());
                likeCount = resp.getLikeCount() == null ? 0L : resp.getLikeCount();
            }
        } catch (Exception ignored) { /* 기본값 유지 */ }

        model.addAttribute("liked", liked);
        model.addAttribute("likeCount", likeCount);

        return "Person/detail";
    }

    @GetMapping("/{id}/update")
    public String updateForm(@PathVariable Integer id, Model model) {
        var entity = personService.get(id);
        model.addAttribute("personDTO", toDTO(entity));
        return "Person/update";
    }

}
