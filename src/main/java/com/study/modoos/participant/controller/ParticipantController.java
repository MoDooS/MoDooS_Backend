package com.study.modoos.participant.controller;

import com.study.modoos.common.CurrentUser;
import com.study.modoos.common.response.NormalResponse;
import com.study.modoos.member.entity.Member;
import com.study.modoos.participant.response.ParticipantResponse;
import com.study.modoos.participant.response.StandbyResponse;
import com.study.modoos.participant.service.ParticipantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/participant")
public class ParticipantController {

    private final ParticipantService participantService;

    @GetMapping("/apply/{studyId}")
    public ResponseEntity<StandbyResponse> applyStudy (@CurrentUser Member member,
                                                       @PathVariable(value = "studyId") Long studyId){
        return ResponseEntity.ok(participantService.applyStudy(member, studyId));
    }

    @GetMapping("/accept/{standbyId}")
    public ResponseEntity<ParticipantResponse> acceptApplication (@CurrentUser Member member, @PathVariable Long standbyId) {
        return ResponseEntity.ok(participantService.acceptApplication(member, standbyId));
    }

    @GetMapping("/reject/{standbyId}")
    public ResponseEntity<NormalResponse> rejectApplication(@CurrentUser Member member, @PathVariable Long standbyId) {
        participantService.rejectApplication(member, standbyId);
        return ResponseEntity.ok(NormalResponse.success());
    }
}
