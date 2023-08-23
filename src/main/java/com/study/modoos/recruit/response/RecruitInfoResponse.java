package com.study.modoos.recruit.response;

import com.study.modoos.member.entity.Campus;
import com.study.modoos.study.entity.Category;
import com.study.modoos.study.entity.Channel;
import com.study.modoos.study.entity.Study;
import com.study.modoos.study.entity.StudyStatus;
import com.study.modoos.study.response.StudyParticipantResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecruitInfoResponse {
    private Long id;

    private Long leader_id;

    private String leader_nickname;

    private String leader_ranking;

    private String title;

    private String description;

    private StudyStatus status;

    private Category category;

    private Campus campus;

    private int recruits_count;

    private int participants_count;

    private Channel channel;

    private LocalDate recruit_deadline;

    private LocalDate expected_start_at;

    private LocalDate expected_end_at;

    private String link;

    private String contact;

    private boolean isWritten;

    private List<TodoResponse> checkList;

    private List<StudyParticipantResponse> participantList;

    private int late;

    private int absent;

    private int out;

    private boolean isHeart;

    private LocalDateTime createdAt;

    public static RecruitInfoResponse of(Study study, boolean isWritten, List<TodoResponse> checkList,
                                         List<StudyParticipantResponse> participantList,
                                         boolean isHeart) {
        return RecruitInfoResponse.builder()
                .id(study.getId())
                .leader_id(study.getLeader().getId())
                .leader_nickname(study.getLeader().getNickname())
                .leader_ranking(study.getLeader().getRanking())
                .title(study.getTitle())
                .description(study.getDescription())
                .status(study.getStatus())
                .category(study.getCategory())
                .campus(study.getCampus())
                .recruits_count(study.getRecruits_count())
                .participants_count(study.getParticipants_count())
                .channel(study.getChannel())
                .recruit_deadline(study.getRecruit_deadline())
                .expected_start_at(study.getExpected_start_at())
                .expected_end_at(study.getExpected_end_at())
                .link(study.getLink())
                .contact(study.getContact())
                .isWritten(isWritten)
                .checkList(checkList)
                .participantList(participantList)
                .late(study.getLate())
                .absent(study.getAbsent())
                .out(study.getOut())
                .isHeart(isHeart)
                .createdAt(study.getCreatedAt())
                .build();
    }
}
