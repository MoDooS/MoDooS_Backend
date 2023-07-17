package com.study.modoos.member.service;

import com.study.modoos.common.exception.BadRequestException;
import com.study.modoos.member.entity.Campus;
import com.study.modoos.member.entity.Member;
import com.study.modoos.member.repository.MemberRepository;
import com.study.modoos.member.request.MemberInfoRequest;
import com.study.modoos.member.response.MemberInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberInfoResponse getMemberInfo(Member currentMember) {
        return memberRepository.findById(currentMember.getId())
                .map(MemberInfoResponse::of)
                .orElseThrow(() -> new IllegalArgumentException("유저 정보를 찾지 못했습니다."));
    }

    public void updateMemberInfo(Member currentMember, MemberInfoRequest memberInfoRequest) {
        Long id = currentMember.getId();
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("유저 정보를 찾지 못했습니다."));

        member.updateNickname(memberInfoRequest.getNickname());
        String campus = memberInfoRequest.getCampus();
        member.updateCampus(Campus.of(campus));
        memberRepository.save(member);
    }

    public void join(Member member) {
        if (isDuplicated(member.getNickname())) {
            throw new BadRequestException("이미 존재하는 닉네임입니다.");
        }
        memberRepository.save(member);
    }

    public boolean nicknameCheck(String nickname) {
        return isDuplicated(nickname);
    }

    private boolean isDuplicated(String nickname){
        return memberRepository.findByNickname(nickname).isPresent();
    }

    public Member findByEmail(String email){
        return memberRepository.findByEmail(email)
                .orElseThrow(()-> new BadRequestException("존재하지 않은 이메일입니다."));
    }
}
