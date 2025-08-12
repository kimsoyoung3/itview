package com.example.itview_spring.DTO;

import lombok.*;

/**
 * 목록 하단에 페이지 정보
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageInfoDTO {
    private int start;  //시작페이지번호
    private int end;    //끝페이지번호
    private int prev;   //이전페이지번호
    private int current;//현재페이지번호
    private int next;   //다음페이지번호
    private int last;   //마지막페이지번호
}
