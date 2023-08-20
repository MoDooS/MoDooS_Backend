package com.study.modoos.study.response;

import com.study.modoos.recruit.response.TodoResponse;
import com.study.modoos.study.entity.Study;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BeforeFeedbackResponse {
    private Long id;    //스터디 id
    private int turn;   //현재 회차
    private List<TodoResponse> checkList;   //체크리스트
    private List<StudyParticipantResponse> participantResponseList; //참여자 list
    private boolean isLeader;

    public static BeforeFeedbackResponse of(Study study, List<TodoResponse> todoResponses, List<StudyParticipantResponse> participantResponseList,
                                            boolean isLeader) {
        return BeforeFeedbackResponse.builder()
                .id(study.getId())
                .turn(study.getCurrent_turn())
                .checkList(todoResponses)
                .participantResponseList(participantResponseList)
                .isLeader(isLeader)
                .build();
    }
}
