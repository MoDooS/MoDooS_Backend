package com.study.modoos.recruit.controller;

import com.study.modoos.common.CurrentUser;
import com.study.modoos.common.response.NormalResponse;
import com.study.modoos.member.entity.Member;
import com.study.modoos.recruit.request.ChangeRecruitRequest;
import com.study.modoos.recruit.request.RecruitRequest;
import com.study.modoos.recruit.response.RecruitResponse;
import com.study.modoos.recruit.service.RecruitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recruit")
public class RecruitController {
    private final RecruitService recruitService;

    @PostMapping("/post")
    public ResponseEntity<RecruitResponse> createRecruit(@CurrentUser Member member, @RequestBody RecruitRequest recruitRequest) {
        return ResponseEntity.ok(recruitService.postRecruit(member, recruitRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecruitResponse> changeRecruit(@CurrentUser Member member, @PathVariable(value = "id") Long id, @RequestBody ChangeRecruitRequest request) {
        return ResponseEntity.ok(recruitService.changeRecruit(member, id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<NormalResponse> deleteRecruit(@CurrentUser Member member, @PathVariable(value = "id") Long id) {
        recruitService.deleteRecruit(member, id);
        return ResponseEntity.ok(NormalResponse.success());
    }
}
