package com.study.modoos.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    STUDY_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 스터디를 찾지 못했습니다.", "요청한 스터디가 유효한지 확인해주세요"),
    STUDY_NOT_EDIT(HttpStatus.NOT_FOUND, "해당 스터디는 이미 생성 완료되었습니다.", "생성 완료된 스터디는 수정, 또는 삭제할 수 없습니다. 스터디가 유효한지 확인해주세요."),
    RULE_NOT_FOUND(HttpStatus.NOT_FOUND, "스터디와 연결된 규칙 정보를 찾지 못했습니다.", "요청한 스터디의 규칙 정보가 유효한지 확인해주세요"),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저 정보를 찾지 못했습니다.", "email 과 password 를 올바르게 입력했는지 확인해주세요"),
    UNAUTHORIZED_MEMBER(HttpStatus.UNAUTHORIZED, "email 또는 비밀번호가 맞지 않습니다.", "다른 이메일 또는 비밀번호를 사용해야합니다."),
    FORBIDDEN_ARTICLE(HttpStatus.FORBIDDEN, "게시글에 수정, 삭제에 대한 권한이 없습니다.", "잘못된 접근입니다. 입력값을 확인해주세요."),
    ALREADY_MEMBER(HttpStatus.CONFLICT, "이미 존재하는 유저 정보입니다.", "다른 이메일 혹은 닉네임을 사용해야합니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 만료됐거나 권한이 없습니다.", "토큰을 재발급 받아야합니다."),
    VALUE_NOT_IN_OPTION(HttpStatus.BAD_REQUEST, "선택지에 없는 값을 사용했습니다.", "선택지에 있는 값을 사용해야 합니다.")

    // HttpStatus 와 Message 을 입력하고 확장s
    ;

    private final HttpStatus httpStatus;
    private final String message;
    private final String solution;
}
