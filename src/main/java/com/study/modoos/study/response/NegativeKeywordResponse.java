package com.study.modoos.study.response;

import com.study.modoos.feedback.entity.Negative;
import com.study.modoos.study.entity.NegativeKeyword;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NegativeKeywordResponse {
    private long count;

    private Negative negative;

    public static NegativeKeywordResponse of(NegativeKeyword negativeKeyword) {
        return NegativeKeywordResponse.builder()
                .count(negativeKeyword.getCount())
                .negative(negativeKeyword.getNegative())
                .build();
    }
}
