package com.study.modoos.study.response;

import com.study.modoos.feedback.entity.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PositiveKeywordResponse {

    private int count;

    private Positive positive;
}