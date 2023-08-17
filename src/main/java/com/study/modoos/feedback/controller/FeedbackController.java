package com.study.modoos.feedback.controller;

import com.study.modoos.common.CurrentUser;
import com.study.modoos.feedback.response.MemberFeedbackResponse;
import com.study.modoos.feedback.service.FeedbackService;
import com.study.modoos.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/feedbacks")
public class FeedbackController {
    private final FeedbackService feedbackService;

    @GetMapping("/{memberId}")
    public ResponseEntity<MemberFeedbackResponse> getMyFeedbacks(@CurrentUser Member member, @PathVariable(value = "memberId") Long memberId) {
        return ResponseEntity.ok(feedbackService.getMemberFeedbacks(member, memberId));
    }

}
