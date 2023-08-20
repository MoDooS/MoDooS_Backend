package com.study.modoos.member.response;

import com.study.modoos.member.entity.Member;
import com.study.modoos.study.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberProfileResponse {
    private Long memberId;

    private boolean isSelf;

    private String nickname;

    private String ranking;

    private Long score;

    private List<Category> categoryList;

    private int heartCount;

    public static MemberProfileResponse of(Member member, List<Category> categoryList,
                                           int heartCount, boolean isSelf) {
        return MemberProfileResponse.builder()
                .memberId(member.getId())
                .isSelf(isSelf)
                .nickname(member.getNickname())
                .ranking(member.getRanking())
                .score(member.getScore())
                .categoryList(categoryList)
                .heartCount(heartCount)
                .build();
    }
}
