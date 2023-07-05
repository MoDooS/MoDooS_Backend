package com.study.modoos.member.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    USER("사용자","ROLE_USER"),
    MEMBER("회원","ROLE_MEMBER");

    private final String roleName;
    private final String authority;
}
