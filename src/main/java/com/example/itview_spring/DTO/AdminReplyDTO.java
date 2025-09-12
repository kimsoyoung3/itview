package com.example.itview_spring.DTO;

import com.example.itview_spring.Constant.Replyable;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminReplyDTO {
    private int id; //댓글 id
    private String text; //댓글 내용
    private LocalDateTime createdAt; //댓글 작성 시각
    private Replyable targetType; //(코멘트/컬렉션)
    private int targetId; //댓글의 타겟의 id
}
