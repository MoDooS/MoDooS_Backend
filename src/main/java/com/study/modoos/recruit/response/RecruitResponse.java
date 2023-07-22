package com.study.modoos.recruit.response;

import com.study.modoos.member.entity.Campus;
import com.study.modoos.study.entity.Category;
import com.study.modoos.study.entity.Channel;
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

    private Long leader_id;

    private String leader_nickname;

    private String leader_ranking;

    private String title;

    private String description;

    private int status;

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

    public static RecruitResponse of(Study study, boolean isWritten) {
        return RecruitResponse.builder()
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
                .build();
    }
}
