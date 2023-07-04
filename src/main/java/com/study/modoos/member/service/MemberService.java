package com.study.modoos.member.service;

import com.study.modoos.member.entity.Member;
import com.study.modoos.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class MemberService {
    private final MemberRepository memberRepository;

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
