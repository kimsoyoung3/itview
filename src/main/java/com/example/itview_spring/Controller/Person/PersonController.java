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
    @GetMapping("/person/list")
    public String list(@PageableDefault(size = 10) Pageable pageable,
                       @RequestParam(required = false) String keyword,
                       Model model) {

        // 검색어가 있으면 /search/person으로 리다이렉트
        if (StringUtils.hasText(keyword)) {
            String redirectUrl = "/search/person?keyword=" + keyword.trim()
                    + "&page=" + pageable.getPageNumber()
                    + "&size=" + pageable.getPageSize();
            return "redirect:" + redirectUrl;
        }

        Page<PersonDTO> page = personService.list(pageable, null).map(this::toDTO);
        model.addAttribute("page", page);
        model.addAttribute("personList", page.getContent());
        model.addAttribute("keyword", null);
        model.addAttribute("baseUrl", "/person/list"); // 페이지네이션용
        return "Person/list";
    }

    /* ======= (사람) 검색 ======= */
    // /search/person?keyword=홍길동
    @GetMapping("/search/person")
    public String searchPerson(@PageableDefault(size = 10) Pageable pageable,
                               @RequestParam String keyword,
                               Model model) {

        Page<PersonDTO> page = personService.list(pageable, keyword).map(this::toDTO);
        model.addAttribute("page", page);
        model.addAttribute("personList", page.getContent());
        model.addAttribute("keyword", keyword);
        model.addAttribute("baseUrl", "/search/person"); // 페이지네이션용
        return "Person/list"; // 같은 목록 템플릿 재사용
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
        return "Person/register";
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
        return "Person/update";
    }

    /* ======= 수정 저장 (폼은 POST + _method=PUT) ======= */
    @PutMapping("/person/{id}/update")
    public String update(@PathVariable Integer id,
                         @ModelAttribute("personDTO") PersonDTO personDTO) {
        personService.update(id, toEntity(personDTO));
        return "redirect:/person/list";
    }

    /* ======= 삭제 (폼은 POST + _method=DELETE) ======= */
    @DeleteMapping("/person/{id}")
    public String delete(@PathVariable Integer id) {
        personService.delete(id);
        return "redirect:/person/list";
    }
}
