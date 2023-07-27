package com.study.modoos.study.entity;

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
public enum Category {
    LANGUAGE("언어"),
    EMPLOYMENT("취업"),
    EXAM("고시/공무원"),
    HOBBY("취미/교양"),
    PROGRAMMING("프로그래밍"),
    ETC("기타");

    private static final Map<String, Category> categoryMap = Stream.of(values())
            .collect(Collectors.toMap(Category::getCategoryName, Function.identity()));

    @JsonValue
    private final String categoryName;

    @JsonCreator
    public static Category resolve(String name) {
        return Optional.ofNullable(categoryMap.get(name))
                .orElseThrow(() -> new ModoosException(ErrorCode.VALUE_NOT_IN_OPTION));

    }
}
