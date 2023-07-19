package com.study.modoos.study.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Channel {
    OFFLINE("오프라인"),
    ONLINE("온라인"),
    BOTH("병행");

    private final String name;
}
