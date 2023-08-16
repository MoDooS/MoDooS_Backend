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
public enum StudyStatus {
    RECRUITING("모집 중"),
    RECRUIT_END("모집 마감"),
    ONGOING("진행 중"),
    STUDY_END("종료");

    private static final Map<String, StudyStatus> statusMap = Stream.of(values())
            .collect(Collectors.toMap(StudyStatus::getStatusName, Function.identity()));

    @JsonValue
    private final String statusName;

    @JsonCreator
    public static StudyStatus resolve(String statusName) {
        return Optional.ofNullable(statusMap.get(statusName))
                .orElseThrow(() -> new ModoosException(ErrorCode.VALUE_NOT_IN_OPTION));
    }

}
