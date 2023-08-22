package com.study.modoos.study.response;

import com.study.modoos.feedback.entity.Positive;
import com.study.modoos.study.entity.PositiveKeyword;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PositiveKeywordResponse {

    private long count;

    private Positive positive;

    public static PositiveKeywordResponse of(PositiveKeyword positiveKeyword) {
        return PositiveKeywordResponse.builder()
                .count(positiveKeyword.getCount())
                .positive(positiveKeyword.getPositive())
                .build();
    }
}