package com.study.modoos.member.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Campus {
    SEOUL("인문"),
    YONGIN("자연"),
    COMMON("공통");
    private final String campusName;

}
