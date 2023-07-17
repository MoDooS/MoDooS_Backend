package com.study.modoos.member.controller;


import com.study.modoos.common.CurrentUser;
import com.study.modoos.common.response.NormalResponse;
import com.study.modoos.member.entity.Member;
import com.study.modoos.member.request.MemberInfoRequest;
import com.study.modoos.member.response.MemberInfoResponse;
import com.study.modoos.member.service.MemberService;
import com.study.modoos.member.request.MemberJoinRequest;
import com.study.modoos.member.request.MemberNicknameCheckRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<NormalResponse> updateMyMemberInfo(@CurrentUser Member member, @RequestBody @Valid MemberInfoRequest memberInfoRequest) {
        memberService.updateMemberInfo(member, memberInfoRequest);
        return ResponseEntity.ok(NormalResponse.success());
    }


    @PostMapping("/join")
    public ResponseEntity<NormalResponse> join(@RequestBody @Valid MemberJoinRequest memberJoinRequest){
        memberService.join(memberJoinRequest.joinMember(passwordEncoder));
        return ResponseEntity.ok(NormalResponse.success());
    }

    @PostMapping("/nickname-check")
    public boolean nicknameCheck(@RequestBody @Valid MemberNicknameCheckRequest memberNicknameCheckRequest){
        return memberService.nicknameCheck(memberNicknameCheckRequest.getNickname());
    }
}
