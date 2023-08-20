package com.study.modoos.study.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAllFeedbackRequest {
    private Long id;    //어떤 스터디
    private int turn;   //현재 평가하는 회차

    private List<CreateOneFeedbackRequest> feedbackList;


}
