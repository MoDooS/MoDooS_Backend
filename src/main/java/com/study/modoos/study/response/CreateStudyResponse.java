package com.study.modoos.study.response;

import com.study.modoos.recruit.response.TodoResponse;
import com.study.modoos.study.entity.Study;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateStudyResponse {
    private Long id;
    private String title;
    private LocalDate expected_start_at;
    private LocalDate expected_end_at;
    private List<TodoResponse> checkList;
    private int absent;
    private int late;
    private int out;

    public static CreateStudyResponse of(Study study, List<TodoResponse> checkList) {
        return CreateStudyResponse.builder()
                .id(study.getId())
                .title(study.getTitle())
                .expected_start_at(study.getExpected_start_at())
                .expected_end_at(study.getExpected_end_at())
                .checkList(checkList)
                .absent(study.getAbsent())
                .late(study.getLate())
                .out(study.getOut())
                .build();
    }
}
