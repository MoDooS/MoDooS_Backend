package com.study.modoos.recruit.request;

import com.study.modoos.member.entity.Campus;
import com.study.modoos.member.entity.Member;
import com.study.modoos.study.entity.Category;
import com.study.modoos.study.entity.Channel;
import com.study.modoos.study.entity.Study;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecruitRequest {
    Campus campus;
    int recruits_count;
    Channel channel;
    Category category;
    LocalDate recruit_deadline;
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

    public Study createRecruit(Member member) {
        return Study.builder()
                .campus(campus)
                .leader(member)
                .recruits_count(recruits_count)
                .channel(channel)
                .category(category)
                .recruit_deadline(recruit_deadline)
                .expected_start_at(expected_start_at)
                .expected_end_at(expected_end_at)
                .contact(contact)
                .link(link)
                .late(late)
                .absent(absent)
                .out(out)
                .rule_content(rule_content)
                .title(title)
                .description(description)
                .build();
    }
}
