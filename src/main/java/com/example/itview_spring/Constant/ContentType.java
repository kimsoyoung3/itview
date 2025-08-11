package com.example.itview_spring.Constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ContentType {
    MOVIE("영화"),
    SERIES("시리즈"),
    BOOK("책"),
    WEBTOON("웹툰"),
    RECORD("음반");

    private final String description;
}
