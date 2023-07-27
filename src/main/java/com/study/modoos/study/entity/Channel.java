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
public enum Channel {
    OFFLINE("오프라인"),
    ONLINE("온라인"),
    BOTH("병행");

    private static final Map<String, Channel> channelMap = Stream.of(values())
            .collect(Collectors.toMap(Channel::getChannelName, Function.identity()));

    @JsonValue
    private final String channelName;

    @JsonCreator
    public static Channel resolve(String name) {
        return Optional.ofNullable(channelMap.get(name))
                .orElseThrow(() -> new ModoosException(ErrorCode.VALUE_NOT_IN_OPTION));
    }
}
