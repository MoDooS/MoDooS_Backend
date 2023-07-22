package com.study.modoos.feedback.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.study.modoos.common.exception.ErrorCode;
import com.study.modoos.common.exception.ModoosException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public enum Positive {
    KIND("친절해요"),
    ACTIVE("적극적으로 참여해요"),
    RESPONSIBILITY("책임감 있어요"),
    PRESENTATION("발표를 잘 해요"),
    COMMUNICATION("소통이 잘 돼요"),
    HELP("잘 도와줘요");

    private static final Map<String, Positive> positiveMap = Stream.of(values())
            .collect(Collectors.toMap(Positive::getKeyword, Function.identity()));

    @JsonValue
    private final String keyword;

    @JsonCreator
    public static Positive resolve(String keyword) {
        return Optional.ofNullable(positiveMap.get(keyword))
                .orElseThrow(() -> new ModoosException(ErrorCode.VALUE_NOT_IN_OPTION));
    }
}
