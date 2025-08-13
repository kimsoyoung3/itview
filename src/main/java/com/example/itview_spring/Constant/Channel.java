package com.example.itview_spring.Constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Channel {
    NETFLIX("넷플릭스"),
    DISNEY_PLUS("디즈니 플러스"),
    WAVE("웨이브"),
    WATCHA("왓챠"),
    TVING("티빙"),
    TVN("tvN"),
    APPLE_TV_PLUS("애플티비플러스"),

    ALADIN("알라딘"),
    YES24("예스24"),
    KYOBO("교보문고"),
    
    NAVER("네이버"),
    KAKAO("카카오");

    private final String description;
}
