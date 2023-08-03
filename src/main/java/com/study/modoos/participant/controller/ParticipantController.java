package com.study.modoos.participant.controller;

import com.study.modoos.common.CurrentUser;
import com.study.modoos.common.response.NormalResponse;
import com.study.modoos.member.entity.Member;
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
    public ResponseEntity<NormalResponse> applyStudy (@CurrentUser Member member,
                                                      @PathVariable(value = "studyId") Long studyId){
        participantService.applyStudy(member, studyId);
        return ResponseEntity.ok(NormalResponse.success());
    }
}
