package com.study.modoos.member.service;

import com.study.modoos.member.entity.Member;
import com.study.modoos.member.repository.MemberRepository;
import com.study.modoos.member.request.MemberInfoRequest;
import com.study.modoos.member.response.MemberInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
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
        member.updateCampus(memberInfoRequest.getCampus());
    }

    public void join(Member member) {
        if (isDuplicated(member.getNickname())) {
            throw new IllegalArgumentException("");
        }
        memberRepository.save(member);
    }

    public boolean nicknameCheck(String nickname) {
        return isDuplicated(nickname);
    }

    private boolean isDuplicated(String nickname){
        boolean isDuplicated = false;
        if(memberRepository.findByNickname(nickname).isPresent()) isDuplicated = true;
        return isDuplicated;
    }
}
