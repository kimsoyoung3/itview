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
    KPOP("KPOP"),
    ANIMATION("애니메이션"),
    SPORTS("스포츠"),
    MYSTERY("미스터리"),
    DOCUMENTARY("다큐멘터리"),
    SF("SF"),
    MUSIC("음악"),
    FAMILY("가족"),
    CONCERT("공연실황"),
    MUSICAL("뮤지컬"),
    BIOPIC("전기"),
    HISTORY("역사"),
    CRIME("범죄"),
    KIDS("키즈");

    private final String genreName;
}
