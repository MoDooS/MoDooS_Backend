package com.study.modoos.study.request;

import com.study.modoos.feedback.entity.Negative;
import com.study.modoos.feedback.entity.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOneFeedbackRequest {
    private Long id;    //피드백 대상 아이디

    private int participate;

    private Positive positive;

    private Negative negative;

    private List<TodoIdResponse> checkList;
}
