package com.study.modoos.participant.response;

import com.study.modoos.participant.entity.Standby;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StandbyResponse {
    private Long standbyId;
    private Long memberId;
    private Long studyId;

    public static StandbyResponse of(Standby standby) {
        return StandbyResponse.builder()
                .standbyId(standby.getId())
                .memberId(standby.getMember().getId())
                .studyId(standby.getStudy().getId())
                .build();
    }
}
