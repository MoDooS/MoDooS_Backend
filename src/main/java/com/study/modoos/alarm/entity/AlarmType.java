package com.study.modoos.alarm.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.study.modoos.common.exception.ErrorCode;
import com.study.modoos.common.exception.ModoosException;
import com.study.modoos.study.entity.StudyStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public enum AlarmType {
    STUDY_CONFRIM("스터디 승인 요청 알림"),
    STUDY_ACCEPT("스터디 승인 수락 알림"),
    STUDY_REJECT("스터디 승인 거절 알림"),
    HEART_STUDY_DEADLINE("찜한 스터디 마감 하루 전 알림"),
    MY_STUDY_COMMENT("내 스터디 댓글 알림"),
    REPLY_OF_MY_COMMENT("대댓글 알림"),
    FEEDBACK_START("피드백 시작 알림"),
    FEEDBACK_END("피드백 종료 2시간 전 알림");

    private static final Map<String, AlarmType> typeMap = Stream.of(values())
            .collect(Collectors.toMap(AlarmType::getTypeName, Function.identity()));

    @JsonValue
    private final String TypeName;

    @JsonCreator
    public static com.study.modoos.alarm.entity.AlarmType resolve(String name) {
        return Optional.ofNullable(typeMap.get(name))
                .orElseThrow(() -> new ModoosException(ErrorCode.VALUE_NOT_IN_OPTION));
    }
}
