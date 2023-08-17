package com.study.modoos.feedback.response;

import com.study.modoos.study.response.NegativeKeywordResponse;
import com.study.modoos.study.response.PositiveKeywordResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MyFeedbackResponse {

    private List<PositiveKeywordResponse> positiveList;

    private List<NegativeKeywordResponse> negativeList;
}
