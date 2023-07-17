package com.study.modoos.member.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.study.modoos.common.exception.BadRequestException;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

@Getter
public enum Campus {
    SEOUL("인문"),
    YONGIN("자연"),
    COMMON("공통");
    private final String campusName;

    Campus(String campusName) {
        this.campusName = campusName;
    }

    @JsonCreator
    public static Campus of(String campusName) {
        return Arrays.stream(Campus.values())
                .filter(campus -> Objects.nonNull(campusName))
                .filter(campus -> Objects.equals(campus.getCampusName(), campusName))
                .findFirst()
                .orElseThrow(() -> new BadRequestException("인문 / 자연 / 공통 중에 선택하시오."));
    }
}
