package com.study.modoos.recruit.response;

import com.study.modoos.study.entity.Category;
import com.study.modoos.study.entity.Study;
import com.study.modoos.study.entity.StudyStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecruitListInfoResponse {
    private Long id;

    private Long leader_id;

    private String leader_nickname;

    private String leader_ranking;

    private String title;

    private StudyStatus status;

    private Category category;

    private int recruits_count;

    private int participants_count;

    private LocalDate recruit_deadline;

    public static RecruitListInfoResponse of(Study study) {
        return RecruitListInfoResponse.builder()
                .id(study.getId())
                .leader_id(study.getLeader().getId())
                .leader_nickname(study.getLeader().getNickname())
                .leader_ranking(study.getLeader().getRanking())
                .title(study.getTitle())
                .status(study.getStatus())
                .category(study.getCategory())
                .recruits_count(study.getRecruits_count())
                .participants_count(study.getParticipants_count())
                .recruit_deadline(study.getRecruit_deadline())
                .build();
    }
}
