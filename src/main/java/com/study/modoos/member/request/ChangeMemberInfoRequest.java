package com.study.modoos.member.request;

import com.study.modoos.member.entity.Campus;
import com.study.modoos.member.entity.Department;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChangeMemberInfoRequest {
    private String nickname;

    private Campus campus;

    private Department department;
}
