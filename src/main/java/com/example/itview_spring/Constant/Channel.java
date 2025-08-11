package com.example.itview_spring.Constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Channel {
    NETFLIX("넷플릭스"),
    DISNEY_PLUS("디즈니 플러스"),
    WAVE("웨이브"),
    WATCHA("왓챠");

    private final String description;
}
