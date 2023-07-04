package com.study.modoos.member.controller;

import com.study.modoos.member.dto.MemberJoinRequest;
import com.study.modoos.member.dto.MemberNicknameCheckRequest;
import com.study.modoos.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/join")
    public void join(@RequestBody @Valid MemberJoinRequest memberJoinRequest){
        memberService.join(memberJoinRequest.joinMember(passwordEncoder));
    }

    @PostMapping("/nickname-check")
    public boolean nicknameCheck(@RequestBody @Valid MemberNicknameCheckRequest memberNicknameCheckRequest){
        return memberService.nicknameCheck(memberNicknameCheckRequest.getNickname());
    }

}
