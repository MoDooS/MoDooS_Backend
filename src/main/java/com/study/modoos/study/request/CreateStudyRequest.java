package com.study.modoos.study.request;

import com.study.modoos.recruit.request.TodoRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateStudyRequest {
    List<TodoRequest> checkList;
    private Long id;
    private LocalDate start_at;
    private LocalDate end_at;
    private LocalTime studyTime;
    private LocalTime endTime;
    private int period; //주기
    private int total_turn; //총 회차
    private int absent;
    private int late;
    private int out;
}
