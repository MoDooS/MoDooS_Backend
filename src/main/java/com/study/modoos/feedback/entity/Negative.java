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
public enum Negative {

    UNKIND("태도가 불친절해요"),
    ABSENT("출석이 아쉬워요"),
    LACK_OF_COMMUNICATION("소통이 아쉬워요"),
    ACQUIESCENT("자기 주장이 약해요"),
    LACK_OF_RESPONSIBILITY("책임감이 부족해요"),
    LACK_OF_CONSIDERATION("배려심이 아쉬워요");

    private static final Map<String, Negative> negativeMap = Stream.of(values())
            .collect(Collectors.toMap(Negative::getKeyword, Function.identity()));

    @JsonValue
    private final String keyword;

    @JsonCreator
    public static Negative resolve(String keyword) {
        return Optional.ofNullable(negativeMap.get(keyword))
                .orElseThrow(() -> new ModoosException(ErrorCode.VALUE_NOT_IN_OPTION));
    }
}
