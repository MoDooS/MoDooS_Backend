package com.study.modoos.study.controller;

import com.study.modoos.common.CurrentUser;
import com.study.modoos.member.entity.Member;
import com.study.modoos.recruit.response.RecruitIdResponse;
import com.study.modoos.study.request.CreateAllAttendanceRequest;
import com.study.modoos.study.request.CreateAllFeedbackRequest;
import com.study.modoos.study.request.CreateStudyRequest;
import com.study.modoos.study.response.BeforeFeedbackResponse;
import com.study.modoos.study.response.CreateStudyResponse;
import com.study.modoos.study.response.StudyInfoResponse;
import com.study.modoos.study.service.StudyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/study")
public class StudyController {

    private final StudyService studyService;

    @GetMapping("/create/{id}")
    public ResponseEntity<CreateStudyResponse> getCreateInfo(@CurrentUser Member member, @PathVariable(value = "id") Long id) {
        return ResponseEntity.ok(studyService.beforeCreate(member, id));
    }

    @PostMapping("/create")
    public ResponseEntity<RecruitIdResponse> createStudy(@CurrentUser Member member, @RequestBody CreateStudyRequest request) {
        return ResponseEntity.ok(studyService.createStudy(member, request));
    }

    @PostMapping("/attend/{id}")
    public ResponseEntity<RecruitIdResponse> checkAttendance(@CurrentUser Member member, @PathVariable(value = "id") Long id,
                                                             @RequestBody CreateAllAttendanceRequest request) {
        return ResponseEntity.ok(studyService.checkAttendance(member, id, request));
    }

    @GetMapping("/feedback/{id}/{turn}")
    public ResponseEntity<BeforeFeedbackResponse> getFeedbackInfo(@CurrentUser Member member, @PathVariable(value = "id") Long id,
                                                                  @PathVariable(value = "turn") int turn) {
        return ResponseEntity.ok(studyService.beforeFeedback(member, id, turn));
    }

    @PostMapping("/feedback/{id}")
    public ResponseEntity<RecruitIdResponse> createFeedback(@CurrentUser Member member, @RequestBody CreateAllFeedbackRequest request) {
        return ResponseEntity.ok(studyService.createFeedback(member, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudyInfoResponse> getStudyInfo(@CurrentUser Member member, @PathVariable(value = "id") Long id) {
        return ResponseEntity.ok(studyService.getStudyInfo(member, id));
    }
}
