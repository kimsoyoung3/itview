package com.example.itview_spring.Controller.Reply;

import com.example.itview_spring.DTO.AdminReplyDTO;
import com.example.itview_spring.DTO.AdminUserDTO;
import com.example.itview_spring.Service.ReplyService;
import com.example.itview_spring.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.example.itview_spring.Constant.Replyable.COLLECTION;
import static com.example.itview_spring.Constant.Replyable.COMMENT;

@Controller
@RequiredArgsConstructor
public class ReplyController {
    private final UserService userService;
    private final ReplyService replyService;
    @Value("${userpage}")
    private String userpage;

    @GetMapping("/reply/{userid}/comment")
    public String listComment(@PathVariable("userid") int id,
                              @PageableDefault(page = 0, size = 10) Pageable pageable,
                              Model model) {

        // 사용자 상세 정보 조회
        AdminUserDTO userDetail = userService.read(id);

        // '코멘트' 타입의 댓글만 필터링하여 조회
        Page<AdminReplyDTO> repliesPage = replyService.list(id, pageable, COMMENT);

        // 페이지 블록 계산
        int currentPage = repliesPage.getNumber();
        int blockSize = 10;
        int startPage = (int) (Math.floor(currentPage / blockSize) * blockSize);
        int endPage = Math.min(startPage + blockSize - 1, repliesPage.getTotalPages() - 1);

        // 모델에 데이터 추가
        model.addAttribute("user", userDetail);
        model.addAttribute("replies", repliesPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        model.addAttribute("userpage", userpage);

        return "reply/list_comment";
    }

    @GetMapping("/reply/{userid}/collection")
    public String listCollection(@PathVariable("userid") int id,
                                 @PageableDefault(page = 0, size = 10) Pageable pageable,
                                 Model model) {

        AdminUserDTO userDetail = userService.read(id);
        Page<AdminReplyDTO> repliesPage = replyService.list(id, pageable, COLLECTION);

        // 1. Calculate the page block values
        int currentPage = repliesPage.getNumber();
        int blockSize = 10;
        int startPage = (int) (Math.floor(currentPage / blockSize) * blockSize);
        int endPage = Math.min(startPage + blockSize - 1, repliesPage.getTotalPages() - 1);

        // 2. Add the calculated values and other necessary attributes to the model
        model.addAttribute("user", userDetail);
        model.addAttribute("replies", repliesPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        model.addAttribute("userpage", userpage);

        return "reply/list_collection";
    }

    @DeleteMapping("/reply/{id}")
    public String deleteReply(@PathVariable("id") int id,
                              @RequestParam("userId") int userId,
                              @RequestParam("targetType") String targetType,
                              RedirectAttributes redirectAttributes) {

        replyService.delete(id);
        redirectAttributes.addFlashAttribute("message", "댓글이 성공적으로 삭제되었습니다.");

        // 댓글 타입에 따라 올바른 URL로 리다이렉트합니다.
        if ("COLLECTION".equalsIgnoreCase(targetType)) {
            return "redirect:/reply/" + userId + "/collection";
        } else {
            return "redirect:/reply/" + userId + "/comment";
        }
    }
}
