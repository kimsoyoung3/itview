package com.example.itview_spring.Constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Genre {
    ACTION("액션"),
    ADVENTURE("어드벤처"),
    COMEDY("코미디"),
    DRAMA("드라마"),
    THRILLER("스릴러"),
    HORROR("호러");

    private final String genreName;
}
