package com.study.modoos.study.response;

import com.study.modoos.feedback.entity.Attendance;
import com.study.modoos.participant.entity.Participant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudyParticipantResponse {
    private List<Attendance> attendanceList;
    private Long id;    //참가자 아이디
    private String nickname;
    private String ranking;
    private int outCount;

    public static StudyParticipantResponse of(Participant participant, List<Attendance> attendanceList) {
        return StudyParticipantResponse.builder()
                .id(participant.getMember().getId())
                .nickname(participant.getMember().getNickname())
                .ranking(participant.getMember().getRanking())
                .outCount(participant.getOut())
                .attendanceList(attendanceList)
                .build();
    }
}
