package com.example.itview_spring.Controller.Content;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.itview_spring.DTO.CreditDTO;
import com.example.itview_spring.DTO.PersonDTO;
import com.example.itview_spring.Entity.PersonEntity;
import com.example.itview_spring.Service.CreditService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CreditController {

    private final CreditService creditService;

    // 1) 컨텐츠 ID 기준 크레딧 전체 조회 (페이징)
    @GetMapping("/content/{contentId}")
    public ResponseEntity<Page<CreditDTO>> getCreditsByContentId(
            @PathVariable Integer contentId,
            @PageableDefault(size = 12) Pageable pageable
    ) {
        try {
            Page<CreditDTO> page = creditService.getCreditByContentId(pageable, contentId);
            return ResponseEntity.ok(page);
        } catch (NoSuchElementException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    // 2) 크레딧 개별 조회
    @GetMapping("/credit/{creditId}")
    public ResponseEntity<CreditDTO> getCreditById(@PathVariable Integer creditId) {
        try {
            CreditDTO creditDTO = creditService.getCreditById(creditId);
            return ResponseEntity.ok(creditDTO);
        } catch (NoSuchElementException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 크레딧 등록/수정 폼(get)
     */
    @GetMapping("/content/{contentId}/credit")
    public String creditForm(@PathVariable("contentId") Integer contentId,
                             @RequestParam(value = "id", required = false) Integer creditId,
                             Model model,
                             RedirectAttributes redirectAttributes) {

        // 1️⃣ 단일 CreditDTO 조회 (수정 모드)
        CreditDTO creditDTO;
        if (creditId != null) {
            // 수정 모드: 기존 크레딧 조회
            creditDTO = creditService.getCreditById(creditId); // 단일 크레딧 조회 메소드 사용
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
        List<CreditDTO> creditList = creditService.getCreditsByContentId(contentId);
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

        try {
            // Person 정보 처리
            creditDTO.setPerson(ensurePerson(creditDTO, redirectAttributes));

            if (creditDTO.getId() == null) {
                // 신규 크레딧 등록
                creditService.addCredit(contentId, creditDTO);
                redirectAttributes.addFlashAttribute("message", "크레딧이 등록되었습니다.");
            } else {
                // 기존 크레딧 수정
                CreditDTO updatedCredit = creditService.updateCredit(creditDTO.getId(), creditDTO);
                if (updatedCredit == null) {
                    redirectAttributes.addFlashAttribute("error", "크레딧 수정에 실패했습니다.");
                    return "redirect:/content/" + contentId + "/credit";
                }
                redirectAttributes.addFlashAttribute("message", "크레딧이 수정되었습니다.");
            }

        } catch (NoSuchElementException e) {
            redirectAttributes.addFlashAttribute("error", "존재하지 않는 인물 또는 콘텐츠입니다.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/content/" + contentId + "/credit";
    }

    private PersonDTO ensurePerson(CreditDTO creditDTO, RedirectAttributes redirectAttributes) {
        if (creditDTO.getPerson() == null || creditDTO.getPerson().getId() == null) {
            if (creditDTO.getPerson() != null && creditDTO.getPerson().getName() != null) {
                PersonEntity person = creditService.getOrCreatePersonByName(creditDTO.getPerson().getName());
                return new PersonDTO(person.getId(), person.getName(), person.getProfile(), person.getJob());
            } else {
                redirectAttributes.addFlashAttribute("error", "인물 정보가 누락되었습니다.");
                throw new IllegalArgumentException("인물 정보가 누락되었습니다.");
            }
        }
        return creditDTO.getPerson();
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

        try {
            // 크레딧 삭제
            creditService.deleteCredit(creditId);
            redirectAttributes.addFlashAttribute("message", "크레딧이 삭제되었습니다.");
        } catch (NoSuchElementException ex) {
            redirectAttributes.addFlashAttribute("error", "존재하지 않는 크레딧 ID입니다.");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", "크레딧 삭제 중 오류가 발생했습니다.");
        }

        return "redirect:/content/" + contentId + "/credit";
    }
}
