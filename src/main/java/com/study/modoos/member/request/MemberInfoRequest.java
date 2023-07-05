package com.study.modoos.member.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberInfoRequest {
    private String nickname;

    private String campus;
}
