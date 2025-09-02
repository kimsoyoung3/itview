package com.example.itview_spring.Controller.Person;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.itview_spring.Config.CustomUserDetails;
import com.example.itview_spring.Constant.ContentType;
import com.example.itview_spring.DTO.PersonResponseDTO;
import com.example.itview_spring.DTO.WorkDTO;
import com.example.itview_spring.DTO.WorkDomainDTO;
import com.example.itview_spring.Service.CreditService;
import com.example.itview_spring.Service.PersonService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RequestMapping("/api/person")
public class PersonRestController {

    private final PersonService personService;
    private final CreditService creditService;

    // 인물 정보 조회
    @GetMapping("{id}")
    public PersonResponseDTO getPersonInfo(@PathVariable("id") Integer personId) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        Integer userId = 0;
        if (auth.getPrincipal() != "anonymousUser") {
            userId = ((CustomUserDetails) auth.getPrincipal()).getId();
        }
        return personService.getPersonResponseDTO(userId, personId);
    }

    // 인물의 작품 참여 분야 조회
    @GetMapping("/{id}/work-domains")
    public List<WorkDomainDTO> getWorkDomainsByPersonId(@PathVariable("id") Integer personId) {
        return creditService.getWorkDomainsByPersonId(personId);
    }

    // 분야별 페이징 조회
    @GetMapping("/{id}/work")
    public Page<WorkDTO> getWorks(@PageableDefault(page = 1) Pageable pageable,
                           @PathVariable("id") Integer personId,
                           @RequestParam("type") ContentType contentType,
                           @RequestParam("department") String department) {
        return creditService.getWorks(pageable.getPageNumber(), personId, contentType, department);
    }

    // 인물에 좋아요 등록
    @PostMapping("/{id}/like")
    public void likePerson(@PathVariable("id") Integer personId, @AuthenticationPrincipal CustomUserDetails user) {
        try {
            personService.likePerson(user.getId(), personId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 인물에 좋아요 취소
    @DeleteMapping("/{id}/like")
    public void unlikePerson(@PathVariable("id") Integer personId, @AuthenticationPrincipal CustomUserDetails user) {
        try {
            personService.unlikePerson(user.getId(), personId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
