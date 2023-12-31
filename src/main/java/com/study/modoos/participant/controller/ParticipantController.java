package com.study.modoos.participant.controller;

import com.study.modoos.common.CurrentUser;
import com.study.modoos.common.response.NormalResponse;
import com.study.modoos.member.entity.Member;
import com.study.modoos.participant.response.AllApplicationResponse;
import com.study.modoos.participant.response.ParticipantResponse;
import com.study.modoos.participant.response.StandbyResponse;
import com.study.modoos.participant.service.ParticipantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/participant")
public class ParticipantController {

    private final ParticipantService participantService;


    @GetMapping("/apply/all")
    public ResponseEntity<Slice<AllApplicationResponse>> getAllApply(@CurrentUser Member member,
                                                                     @PageableDefault(sort = "createdAt", direction = DESC) Pageable pageable) {
        return ResponseEntity.ok(participantService.getAllApply(member, pageable));
    }

    @GetMapping("/apply/{studyId}")
    public ResponseEntity<StandbyResponse> applyStudy(@CurrentUser Member member,
                                                      @PathVariable(value = "studyId") Long studyId) {
        return ResponseEntity.ok(participantService.applyStudy(member, studyId));
    }

    @GetMapping("/accept/{standbyId}")
    public ResponseEntity<ParticipantResponse> acceptApplication(@CurrentUser Member member, @PathVariable(value = "standbyId") Long standbyId) {
        return ResponseEntity.ok(participantService.acceptApplication(member, standbyId));
    }

    @GetMapping("/reject/{standbyId}")
    public ResponseEntity<NormalResponse> rejectApplication(@CurrentUser Member member, @PathVariable(value = "standbyId") Long standbyId) {
        participantService.rejectApplication(member, standbyId);
        return ResponseEntity.ok(NormalResponse.success());
    }
}
