package com.study.modoos.member.controller;

import com.study.modoos.common.response.NormalResponse;
import com.study.modoos.member.entity.Member;
import com.study.modoos.member.request.MemberInfoRequest;
import com.study.modoos.member.response.MemberInfoResponse;
import com.study.modoos.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/myInfo")
    public ResponseEntity<MemberInfoResponse> getMyMemberInfo(Member member) {
        MemberInfoResponse response = memberService.getMemberInfo(member);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/myInfo")
    public ResponseEntity<NormalResponse> updateMyMemberInfo(Member member, MemberInfoRequest memberInfoRequest) {
        memberService.updateMemberInfo(member, memberInfoRequest);
        return ResponseEntity.ok(NormalResponse.success());
    }
}
