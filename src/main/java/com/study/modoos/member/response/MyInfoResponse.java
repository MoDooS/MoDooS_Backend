package com.study.modoos.member.response;

import com.study.modoos.member.entity.Campus;
import com.study.modoos.member.entity.Department;
import com.study.modoos.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MyInfoResponse {
    private Long memberId;

    private String nickname;

    private String email;

    private Campus campus;

    private Department department;


    public static MyInfoResponse of(Member member) {
        return MyInfoResponse.builder()
                .memberId(member.getId())
                .nickname(member.getNickname())
                .email(member.getEmail())
                .campus(member.getCampus())
                .department(member.getDepartment())
                .build();
    }
}
