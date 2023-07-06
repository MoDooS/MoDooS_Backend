package com.study.modoos.feedback.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Negative {

    UNKIND("태도가 불친절해요"),
    ABSENT("출석이 아쉬워요"),
    LACK_OF_COMMUNICATION("소통이 아쉬워요"),
    ACQUIESCENT("자기 주장이 약해요"),
    LACK_OF_RESPONSIBILITY("책임감이 부족해요"),
    LACK_OF_CONSIDERATION("배려심이 아쉬워요");

    private final String keyword;
}
