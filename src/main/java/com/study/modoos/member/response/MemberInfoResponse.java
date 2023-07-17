package com.study.modoos.member.response;

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

    private String nickname;

    private String email;

    private String campus;

    private String ranking;

    private Long score;

    public static MemberInfoResponse of(Member member) {
        return MemberInfoResponse.builder()
                .nickname(member.getNickname())
                .email(member.getEmail())
                .campus(member.getCampus().getCampusName())
                .ranking(member.getRanking())
                .score(member.getScore())
                .build();
    }
}
