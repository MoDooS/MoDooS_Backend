package com.study.modoos.recruit.request;

import com.study.modoos.member.entity.Campus;
import com.study.modoos.study.entity.Category;
import com.study.modoos.study.entity.Channel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeRecruitRequest {
    Campus campus;
    Channel channel;
    Category category;
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
    List<TodoRequest> checkList;
}
