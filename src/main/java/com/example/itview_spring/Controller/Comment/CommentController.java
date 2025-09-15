package com.example.itview_spring.Controller.Comment;

import com.example.itview_spring.DTO.AdminCommentDTO;
import com.example.itview_spring.DTO.AdminUserDTO;
import com.example.itview_spring.Service.CommentService;
import com.example.itview_spring.Service.UserService;
import lombok.RequiredArgsConstructor;
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

@Controller
@RequiredArgsConstructor
public class CommentController {
    private final UserService userService;
    private final CommentService commentService;

    @GetMapping("/comment/{userid}")
    public String listComments(@PathVariable("userid") int id,
                               @PageableDefault(page = 0, size = 10) Pageable pageable,
                               Model model) {

        // 사용자 상세 정보 조회
        AdminUserDTO userDetail = userService.read(id);

        // CommentService를 호출하여 특정 사용자의 코멘트 목록을 페이징하여 조회
        Page<AdminCommentDTO> commentsPage = commentService.list(id, pageable);

        // 페이지 블록 계산
        int currentPage = commentsPage.getNumber();
        int blockSize = 10;
        int startPage = (int) (Math.floor(currentPage / blockSize) * blockSize);
        int endPage = Math.min(startPage + blockSize - 1, commentsPage.getTotalPages() - 1);

        // 모델에 데이터 추가
        model.addAttribute("user", userDetail);
        model.addAttribute("comments", commentsPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "comment/list";
    }

    @DeleteMapping("/comment/{commentId}")
    public String deleteComment(@PathVariable("commentId") int commentId,
                                @RequestParam("userId") int userId,
                                RedirectAttributes redirectAttributes) {
        commentService.delete(commentId);
        redirectAttributes.addFlashAttribute("message", "코멘트가 성공적으로 삭제되었습니다.");

        // 올바른 사용자 ID를 사용하여 리다이렉트합니다.
        return "redirect:/comment/" + userId;
    }
}
