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
public enum Attendance {
    ATTEND("출석", 0),
    LATE("지각", 1),
    ABSENT("결석", 2);


    private static final Map<String, Attendance> attendanceMap = Stream.of(values())
            .collect(Collectors.toMap(Attendance::getKeyword, Function.identity()));

    @JsonValue
    private final String keyword;

    private final int status;

    @JsonCreator
    public static Attendance resolve(String keyword) {
        return Optional.ofNullable(attendanceMap.get(keyword))
                .orElseThrow(() -> new ModoosException(ErrorCode.VALUE_NOT_IN_OPTION));
    }
}
