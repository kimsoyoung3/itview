package com.example.itview_spring.Controller.Content;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
            creditDTO = creditService.getCreditById(creditId);
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

        // Person 정보가 없으면 이름 기준으로 조회 후 없으면 생성
        if ((creditDTO.getPerson() == null || creditDTO.getPerson().getId() == null)
                && creditDTO.getPerson() != null && creditDTO.getPerson().getName() != null) {
            PersonEntity person = creditService.getOrCreatePersonByName(creditDTO.getPerson().getName());
            creditDTO.getPerson().setId(person.getId());
        }

        if (creditDTO.getId() == null) {
            // 신규 등록
            creditService.addCredits(contentId, List.of(creditDTO));
            redirectAttributes.addFlashAttribute("message", "크레딧이 등록되었습니다.");
        } else {
            // 수정 처리
            creditService.updateCredit(contentId, List.of(creditDTO));
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

        creditService.deleteCredit(creditId);

        redirectAttributes.addFlashAttribute("message", "크레딧이 삭제되었습니다.");

        return "redirect:/content/" + contentId + "/credit";
    }
}
