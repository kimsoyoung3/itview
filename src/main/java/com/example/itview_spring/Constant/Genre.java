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
    HORROR("호러"),
    FANTASY("판타지"),
    ROMANCE("로맨스"),
    NATURAL_SCIENCE("자연과학"),
    KPOP("KPOP");

    private final String genreName;
}
