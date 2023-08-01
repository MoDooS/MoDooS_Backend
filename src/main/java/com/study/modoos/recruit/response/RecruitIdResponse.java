package com.study.modoos.recruit.response;

import com.study.modoos.study.entity.Study;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecruitIdResponse {
    private Long id;

    public static RecruitIdResponse of(Study study) {
        return RecruitIdResponse.builder()
                .id(study.getId())
                .build();
    }
}
