package com.example.itview_spring.Constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotiType {
    REPLY("reply"),
    LIKE("like"),
    FOLLOW("follow");

    private final String type; 
}
    