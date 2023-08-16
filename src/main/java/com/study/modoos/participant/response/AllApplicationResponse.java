package com.study.modoos.participant.response;

import com.study.modoos.participant.entity.Standby;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AllApplicationResponse {
    Long memberId;
    String nickName;
    Long studyId;
    String title;

    public static AllApplicationResponse of(Standby standby){
        return AllApplicationResponse.builder()
                .memberId(standby.getMember().getId())
                .nickName(standby.getMember().getNickname())
                .studyId(standby.getStudy().getId())
                .title(standby.getStudy().getTitle())
                .build();
    }

}
