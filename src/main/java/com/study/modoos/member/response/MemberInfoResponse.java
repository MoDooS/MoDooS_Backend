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
public class MemberInfoResponse {
    private Long memberId;

    private String nickname;

    private String email;

    private Campus campus;

    private Department department;

    private String ranking;

    private Long score;

    public static MemberInfoResponse of(Member member) {
        return MemberInfoResponse.builder()
                .memberId(member.getId())
                .nickname(member.getNickname())
                .email(member.getEmail())
                .campus(member.getCampus())
                .department(member.getDepartment())
                .ranking(member.getRanking())
                .score(member.getScore())
                .build();
    }
}
