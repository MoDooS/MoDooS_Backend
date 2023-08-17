package com.study.modoos.study.response;

import com.study.modoos.feedback.entity.Negative;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NegativeKeywordResponse {
    private Long count;

    private Negative negative;
}
