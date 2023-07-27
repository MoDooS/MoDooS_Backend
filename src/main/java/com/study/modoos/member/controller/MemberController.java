package com.study.modoos.member.controller;


import com.study.modoos.common.CurrentUser;
import com.study.modoos.common.response.NormalResponse;
import com.study.modoos.member.entity.Member;
import com.study.modoos.member.request.ChangeMemberInfoRequest;
import com.study.modoos.member.request.MemberJoinRequest;
import com.study.modoos.member.request.MemberNicknameCheckRequest;
import com.study.modoos.member.response.MemberInfoResponse;
import com.study.modoos.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

    private final PasswordEncoder passwordEncoder;
    private final MemberService memberService;

    @GetMapping("/myInfo")
    public ResponseEntity<MemberInfoResponse> getMyMemberInfo(@CurrentUser Member member) {
        MemberInfoResponse response = memberService.getMemberInfo(member);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/myInfo")
    public ResponseEntity<NormalResponse> updateMyMemberInfo(@CurrentUser Member member, @RequestBody @Valid ChangeMemberInfoRequest memberInfoRequest) {
        memberService.updateMemberInfo(member, memberInfoRequest);
        return ResponseEntity.ok(NormalResponse.success());
    }


    @PostMapping("/join")
    public ResponseEntity<NormalResponse> join(@RequestBody @Valid MemberJoinRequest memberJoinRequest) {
        memberService.join(memberJoinRequest.joinMember(passwordEncoder));
        return ResponseEntity.ok(NormalResponse.success());
    }

    @PostMapping("/nickname-check")
    public ResponseEntity<NormalResponse> nicknameCheck(@RequestBody @Valid MemberNicknameCheckRequest memberNicknameCheckRequest) {
        if (memberService.nicknameCheck(memberNicknameCheckRequest.getNickname())) {
            return ResponseEntity.ok(NormalResponse.fail());
        }
        return ResponseEntity.ok(NormalResponse.success());
    }
}
