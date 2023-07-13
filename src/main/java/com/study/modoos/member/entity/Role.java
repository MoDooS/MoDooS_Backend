package com.study.modoos.member.entity;

import lombok.Getter;

@Getter
public enum Role {
    USER("사용자","ROLE_USER"),
    MEMBER("회원","ROLE_MEMBER");

    private final String roleName;
    private final String authority;

    Role(String roleName, String authority){
        this.roleName = roleName;
        this.authority = authority;
    }
}
