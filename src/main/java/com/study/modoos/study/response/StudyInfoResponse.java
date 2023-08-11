package com.study.modoos.study.response;

import com.study.modoos.member.entity.Member;
import com.study.modoos.recruit.response.TodoResponse;
import com.study.modoos.study.entity.Study;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudyInfoResponse {

    private Long id;

    private Long memberId;

    private String title;

    private String description;

    private int participants_count;

    private int absent;

    private int late;

    private int out;

    private LocalDate start_at;

    private LocalDate end_at;

    private LocalTime studyTime;

    private int period;

    private int total_turn;

    private List<TodoResponse> checkList;   //전체 todo리스트

    //현재 회원이 지난 주차 피드백 respons 담기
    private FeedbackResponse feedback;

    private List<StudyParticipantResponse> participantList; //전체 회원의 출석상태, 아웃, 정보 ㅔ

    public static StudyInfoResponse of(Study study, Member member, List<TodoResponse> checkList, FeedbackResponse feedback, List<StudyParticipantResponse> participantList) {
        return StudyInfoResponse.builder()
                .id(study.getId())
                .memberId(member.getId())
                .title(study.getTitle())
                .description(study.getDescription())
                .participants_count(study.getParticipants_count())
                .absent(study.getAbsent())
                .late(study.getLate())
                .out(study.getOut())
                .start_at(study.getStart_at())
                .end_at(study.getEnd_at())
                .studyTime(study.getStudyTime())
                .period(study.getPeriod())
                .total_turn(study.getTotal_turn())
                .checkList(checkList)
                .feedback(feedback)
                .participantList(participantList)
                .build();
    }
}
