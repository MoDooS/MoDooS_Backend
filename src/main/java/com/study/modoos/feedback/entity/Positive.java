package com.study.modoos.feedback.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Positive {
    KIND("친절해요"),
    ACTIVE("적극적으로 참여해요"),
    RESPONSIBILITY("책임감 있어요"),
    PRESENTATION("발표를 잘 해요"),
    COMMUNICATION("소통이 잘 돼요"),
    HELP("잘 도와줘요");

    private final String keyword;
}
