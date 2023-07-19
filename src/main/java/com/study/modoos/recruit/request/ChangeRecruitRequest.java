package com.study.modoos.recruit.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeRecruitRequest {
    String campus;
    String channel;
    String category;
    LocalDate expected_start_at;
    LocalDate expected_end_at;
    String contact;
    String link;
    int late;
    int absent;
    int out;
    String rule_content;
    String title;
    String description;
}
