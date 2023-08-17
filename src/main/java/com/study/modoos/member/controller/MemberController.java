package com.study.modoos.member.controller;


import com.study.modoos.common.CurrentUser;
import com.study.modoos.common.response.NormalResponse;
import com.study.modoos.member.entity.Member;
import com.study.modoos.member.request.ChangeMemberInfoRequest;
import com.study.modoos.member.request.MemberJoinRequest;
import com.study.modoos.member.request.MemberNicknameCheckRequest;
import com.study.modoos.member.response.MemberProfileResponse;
import com.study.modoos.member.response.MyInfoResponse;
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
    public ResponseEntity<MyInfoResponse> getMyMemberInfo(@CurrentUser Member member) {
        MyInfoResponse response = memberService.getMyInfo(member);
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

    @GetMapping("/profile/{memberId}")
    public ResponseEntity<MemberProfileResponse> getMemberProfile(@CurrentUser Member member, @PathVariable(value = "memberId") Long memberId) {
        return ResponseEntity.ok(memberService.getMemberProfile(member, memberId));
    }
}
