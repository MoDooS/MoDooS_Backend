package com.study.modoos.study.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeedbackResponse {

    private int times;

    private List<CheckTodoResponse> checkList;  //통과한 todo리스트

    private List<PositiveKeywordResponse> positiveList;

    private List<NegativeKeywordResponse> negativeList;
}
