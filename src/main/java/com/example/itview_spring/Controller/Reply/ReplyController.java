package com.example.itview_spring.Controller.Reply;

import com.example.itview_spring.DTO.AdminReplyDTO;
import com.example.itview_spring.DTO.AdminUserDTO;
import com.example.itview_spring.Service.ReplyService;
import com.example.itview_spring.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ReplyController {
    private final UserService userService;
    private final ReplyService replyService;

    @GetMapping("/reply/{userid}/comment")
    public String list(@PathVariable("userid") int id,
                                @PageableDefault(page = 0, size = 10) Pageable pageable,
                                Model model) {

        AdminUserDTO userDetail = userService.read(id);
        Page<AdminReplyDTO> repliesPage = replyService.list(id, pageable);

        model.addAttribute("user", userDetail);
        model.addAttribute("replies", repliesPage);

        return "reply/list_comment";
    }
}
