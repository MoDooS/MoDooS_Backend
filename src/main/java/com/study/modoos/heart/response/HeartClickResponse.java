package com.study.modoos.heart.response;

import com.study.modoos.member.entity.Member;
import com.study.modoos.study.entity.Study;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HeartClickResponse {
    Long studyId;
    Long memberId;
    boolean isHearted;

    public static HeartClickResponse of(Member member, Study study, boolean isHearted) {
        return HeartClickResponse.builder()
                .studyId(study.getId())
                .memberId(member.getId())
                .isHearted(isHearted)
                .build();
    }
}
