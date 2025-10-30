package com.example.itview_spring.Constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Replyable {
    COMMENT("코멘트"),
    COLLECTION("컬렉션"),
    REPLY("댓글"),
    PERSON("인물");

    private final String type;
}