package com.study.modoos.study.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Category {
    LANGUAGE("언어"),
    EMPLOYMENT("취업"),
    EXAM("고시/공무원"),
    HOBBY("취미/교양"),
    PROGRAMMING("프로그래밍"),
    ETC("기타");

    private final String name;
}
