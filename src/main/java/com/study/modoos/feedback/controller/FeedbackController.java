package com.study.modoos.feedback.controller;

import com.study.modoos.common.CurrentUser;
import com.study.modoos.feedback.response.MyFeedbackResponse;
import com.study.modoos.feedback.service.FeedbackService;
import com.study.modoos.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/feedbacks")
public class FeedbackController {
    private final FeedbackService feedbackService;

    @GetMapping("/my")
    public ResponseEntity<MyFeedbackResponse> getMyFeedbacks(@CurrentUser Member member) {
        return ResponseEntity.ok(feedbackService.getMyFeedbacks(member));
    }

}
