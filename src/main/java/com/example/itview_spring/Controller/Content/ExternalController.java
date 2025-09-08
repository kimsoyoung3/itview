package com.example.itview_spring.Controller.Content;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.itview_spring.DTO.ExternalServiceDTO;
import com.example.itview_spring.Service.ExternalService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ExternalController {

    private final ExternalService externalService;

    @GetMapping("/content/{contentId}/external")
    public String externalServiceForm(@PathVariable("contentId") Integer contentId,
                                    @RequestParam(value = "id", required = false) Integer externalServiceId,
                                    Model model) {
        // 1️⃣ 수정 모드 vs 등록 모드 구분
        ExternalServiceDTO externalServiceDTO = (externalServiceId != null)
                ? externalService.getExternalServiceById(externalServiceId)  // contentService에서 단일ExternalServiceId 조회 메서드 필요
                : new ExternalServiceDTO();                        // 등록 모드
        model.addAttribute("externalServiceDTO", externalServiceDTO);

        // 2️⃣ 전체ExternalServiceDTO 리스트 조회 (목록)
        List<ExternalServiceDTO> externalServiceList = externalService.getExternalServicesByContentId(contentId);
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
            model.addAttribute("externalServiceList", externalService.getExternalServicesByContentId(contentId));
            model.addAttribute("contentId", contentId);
            return "content/externalForm";  // 다시 입력폼 보여주기
        }

        if (externalServiceDTO.getId() == null) {
            // 신규 등록
            externalService.createExternalService(contentId, externalServiceDTO);
            redirectAttributes.addFlashAttribute("message", "등록되었습니다.");
        } else {
            // 수정 처리
            externalService.updateExternalService(externalServiceDTO.getId(), externalServiceDTO);
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

        externalService.deleteExternalService(externalServiceId); //실제 externalServiceId 기반 삭제
        // ✅ 메시지 추가
        redirectAttributes.addFlashAttribute("message", "삭제되었습니다.");
        // 삭제 후 영상 등록 페이지로 리다이렉트

        return "redirect:/content/" + contentId + "/external";
    }
}
