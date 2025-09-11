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
    ///////0911  인물검색 추가 ////////////////////////////////////////////////////////
//    @GetMapping("/lecture/list")
//    //모든 데이터를 조회
//
//    public String listLecture(@RequestParam(name = "keyword", defaultValue = "") String keyword,@PageableDefault(page=1) Pageable pageable, Model model) {
//        //모든 데이터를 조회
//        //keyword  추가
//        Page<LectureDTO> lectureDTOS =lectureService.getAllLectures(keyword ,pageable);
//        model.addAttribute("lectureDTOS",lectureDTOS);
//
//        PageInfoDTO pageInfoDTO = pageInfo.getPageInfo(lectureDTOS);
//        model.addAttribute("pageInfoDTO", pageInfoDTO);
//        model.addAttribute("keyword", keyword);  //추가
//        return "list" ;
//
//
//
//

//📌 흐름 정리
//
//   1. 인물 검색 버튼 → 모달 오픈
//
//   2. 이름 입력 + 검색 → /content/{contentId}/credit/search-person 호출
//
//   3. JSON 응답(PersonDTO)을 카드 리스트로 표시 (프로필, 이름, 직업, 선택 버튼)
//
//   4. 선택 버튼 클릭 → creditForm의 입력란 자동 채우기 → 모달 닫기
//
//   5. 저장 버튼 클릭 시 선택된 인물 ID와 함께 크레딧 저장


        ///////////////////////////////////////////////////////////////

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

        CreditDTO creditDTO = (creditId != null)
                ? creditService.getCreditById(creditId)
                : new CreditDTO();
        // 1️⃣ 단일 CreditDTO 조회 (수정 모드)

//        if (creditId != null) {
//            // 수정 모드: 기존 크레딧 조회
//            creditDTO = creditService.getCreditById(creditId); // 단일 크레딧 조회 메소드 사용
//            System.out.println("📌 contentId: " + contentId);
//            System.out.println("📌 creditDTO.id: " + creditDTO.getId());
//
//            if (creditDTO.getPerson() == null) {
//                creditDTO.setPerson(new PersonDTO()); // 안전하게 PersonDTO 초기화
//                System.out.println("📌 person.id: " + creditDTO.getPerson().getId());
//                System.out.println("📌 person.name: " + creditDTO.getPerson().getName());
//            }
//        } else {
//            System.out.println("⚠️ person 정보 없음");
//            // 신규 등록 모드: 빈 CreditDTO + PersonDTO 포함
//            creditDTO = new CreditDTO();
//            creditDTO.setPerson(new PersonDTO());
//        }

        // 2️⃣ ContentId도 모델에 전달
        model.addAttribute("contentId", contentId);

        model.addAttribute("creditDTO", creditDTO);

        // 3️⃣ 전체 CreditDTO 리스트 조회 (목록)
        List<CreditDTO> creditList = creditService.getCreditsByContentId(contentId);
        model.addAttribute("creditList", creditList);

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

//        try {
//            // Person 정보 처리
//            creditDTO.setPerson(ensurePerson(creditDTO, redirectAttributes));
//
//            if (creditDTO.getId() == null) {
//                // 신규 크레딧 등록
//                creditService.addCredit(contentId, creditDTO);
//                redirectAttributes.addFlashAttribute("message", "크레딧이 등록되었습니다.");
//            } else {
//                // 기존 크레딧 수정
//                CreditDTO updatedCredit = creditService.updateCredit(creditDTO.getId(), creditDTO);
//                if (updatedCredit == null) {
//                    redirectAttributes.addFlashAttribute("error", "크레딧 수정에 실패했습니다.");
//                    return "redirect:/content/" + contentId + "/credit";
//                }
//                redirectAttributes.addFlashAttribute("message", "크레딧이 수정되었습니다.");
//            }
//
//        } catch (NoSuchElementException e) {
//            redirectAttributes.addFlashAttribute("error", "존재하지 않는 인물 또는 콘텐츠입니다.");
//        } catch (IllegalArgumentException e) {
//            redirectAttributes.addFlashAttribute("error", e.getMessage());
//        }
        try {
            if (creditDTO.getId() == null) {
                creditService.addCredit(contentId, creditDTO);
                redirectAttributes.addFlashAttribute("message", "크레딧 등록 완료");
            } else {
                creditService.updateCredit(creditDTO.getId(), creditDTO);
                redirectAttributes.addFlashAttribute("message", "크레딧 수정 완료");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
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

        try {
            // 크레딧 삭제
            creditService.deleteCredit(creditId);
            redirectAttributes.addFlashAttribute("message", "크레딧이 삭제되었습니다.");
        } catch (NoSuchElementException ex) {
            redirectAttributes.addFlashAttribute("error", "존재하지 않는 크레딧 ID입니다.");
        }
        return "redirect:/content/" + contentId + "/credit";
    }

    // 🔹 인물 검색 (AJAX: JSON 반환)
    @GetMapping("/content/{contentId}/credit/search-person")
    @ResponseBody
    public List<PersonDTO> searchPerson(@PathVariable Integer contentId,
                                        @RequestParam String keyword) {
        return creditService.searchPersons(keyword);
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
}
