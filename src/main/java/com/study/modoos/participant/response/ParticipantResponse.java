package com.study.modoos.participant.response;

import com.study.modoos.participant.entity.Participant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ParticipantResponse {
    private Long standbyId;
    private Long memberId;
    private Long studyId;

    public static ParticipantResponse of(Participant participant) {
        return ParticipantResponse.builder()
                .standbyId(participant.getId())
                .memberId(participant.getMember().getId())
                .studyId(participant.getStudy().getId())
                .build();
    }
}
