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
    WAITER_NOT_FOUND(HttpStatus.NOT_FOUND, "대기자 정보를 찾지 못했습니다.", "standbyId를 확인해주세요"),
    UNAUTHORIZED_MEMBER(HttpStatus.UNAUTHORIZED, "email 또는 비밀번호가 맞지 않습니다.", "다른 이메일 또는 비밀번호를 사용해야합니다."),
    FORBIDDEN_ARTICLE(HttpStatus.FORBIDDEN, "게시글에 수정, 삭제에 대한 권한이 없습니다.", "잘못된 접근입니다. 입력값을 확인해주세요."),
    FORBIDDEN_STUDY_ACCEPT(HttpStatus.FORBIDDEN, "스터디 관리에 대한 권한이 없습니다.", "잘못된 접근입니다. 스터디 리더인지 확인해주세요"),
    ALREADY_MEMBER(HttpStatus.CONFLICT, "이미 존재하는 유저 정보입니다.", "다른 이메일 혹은 닉네임을 사용해야합니다."),
    ALREADY_PARTICIPANT(HttpStatus.CONFLICT, "해당 스터디에 이미 참여중입니다.", "다른 스터디를 신청해주세요"),
    ALREADY_STANDBY(HttpStatus.CONFLICT, "해당 스터디에 이미 참여 신청하였습니다.", "다른 스터디 신청해주세요"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 만료됐거나 권한이 없습니다.", "토큰을 재발급 받아야합니다."),
    VALUE_NOT_IN_OPTION(HttpStatus.BAD_REQUEST, "선택지에 없는 값을 사용했습니다.", "선택지에 있는 값을 사용해야 합니다."),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 comment id 입니다.", "올바른 comment id 인지 확인해주세요."),
    INVALID_WRITER(HttpStatus.UNAUTHORIZED, "댓글 작성자와 호출자(현재 사용자)의 정보가 다릅니다", "댓글 작성자를 확인해주세요"),
    INVALID_DELETE(HttpStatus.UNAUTHORIZED, "댓글 삭제자와 호출자(현재 사용자)의 정보가 다릅니다.", "댓글 삭제자를 확인해주세요"),
    INVALID_PARENT_ID(HttpStatus.UNAUTHORIZED, "부모 댓글의 스터디 공고글과 현재 작성하는 스터디 공고글이 다릅니다.", "부모 댓글 id를 확인해주세요"),
    TODO_NOT_FOUND(HttpStatus.NOT_FOUND, "체크리스트 항목을 찾을 수 있습니다.", "체크리스트 항목 id가 올바른지 확인해주세요."),
    INVALID_STUDY(HttpStatus.NOT_FOUND, "해당 스터디의 참여 인원 정보를 가져올 수 없습니다.", "스터디의 참여인원이 있는지 확인해주세요"),
    FULL_PARTICIPANT(HttpStatus.CONFLICT, "해당 스터디의 참여 인원이 꽉 찼습니다.", "지금은 해당 스터디에 참여신청이 불가합니다."),
    STUDY_STATUS(HttpStatus.NOT_FOUND, "스터디 상태 입력값이 잘못되었습니다.", "상태값이 null/모집중/모집완료/생성완료 중에 있는지 확인해주세요"),
    MEMBER_NOT_IN_STUDY(HttpStatus.FORBIDDEN, "해당 스터디에 참여인원이 아닙니다.", "스터디에 참여중인지 확인해주세요."),
    RECEIVER_NOT_IN_STUDY(HttpStatus.FORBIDDEN, "평가 회원이 해당 스터디에 참여 인원이 아닙니다.", "스터디에 참여중인지 확인해주세요"),
    NOT_EVALUATION_PERIOD(HttpStatus.FORBIDDEN, "평기 기간이 아닙니다.", "평가기간을 다시 한 번 확인해주세요."),
    MEMBER_IS_ABSENT(HttpStatus.FORBIDDEN, "결석한 참여자는 평가할 수 없습니다.", "이번 회차 출결을 확인해주세요."),
    UNABLE_TO_START_ON_THE_DAY(HttpStatus.FORBIDDEN, "스터디 시작일은 당잉이 될 수 없습니다.", "스터디 시작일을 변경해주세요."),
    ALREADY_FEEDBACK(HttpStatus.CONFLICT, "이미 평가하였습니다.", "평가 주차와 이전 평가 여부를 다시 한 번 확인해주세요."),
    INVALID_COOKIE_NAME(HttpStatus.NOT_FOUND, "JwtFilter > resolveToken 에서 쿠키 이름이 다릅니다", "cookie.getName()으로 쿠키 이름을 확인해주세요");


    private final HttpStatus httpStatus;
    private final String message;
    private final String solution;
}
