package com.study.modoos.ranking.response;

import com.study.modoos.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RankingResponse {
    private Long rankingNumber;
    private Long memberId;
    private String nickName;
    private String ranking;
    private Long score;

    public static RankingResponse of(Member member) {
        return RankingResponse.builder()
                .memberId(member.getId())
                .nickName(member.getNickname())
                .ranking(member.getRanking())
                .score(member.getScore())
                .build();
    }

    public void setRankingNumber(long rankingNumber) {
        this.rankingNumber = rankingNumber;
    }
}
