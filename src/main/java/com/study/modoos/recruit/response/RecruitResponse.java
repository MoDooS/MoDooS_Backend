package com.study.modoos.recruit.response;

import com.study.modoos.member.entity.Member;
import com.study.modoos.study.entity.Study;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecruitResponse {
    private Long id;

    private Member leader;

    private String title;

    private String description;

    private int status;

    private String category;

    private String campus;

    private int recruits_count;

    private int participants_count;

    private String channel;

    private LocalDate recruit_deadline;

    private LocalDate expected_start_at;

    private LocalDate expected_end_at;

    private String link;

    public static RecruitResponse of(Study study, boolean isWritten) {
        return RecruitResponse.builder()
                .id(study.getId())
                .leader(study.getLeader())
                .title(study.getTitle())
                .description(study.getDescription())
                .status(study.getStatus())
                .category(study.getCategory().getName())
                .campus(study.getCampus().getCampusName())
                .recruits_count(study.getRecruits_count())
                .participants_count(study.getParticipants_count())
                .channel(study.getChannel().getName())
                .recruit_deadline(study.getRecruit_deadline())
                .expected_start_at(study.getExpected_start_at())
                .expected_end_at(study.getExpected_end_at())
                .link(study.getLink())
                .build();
    }
}
