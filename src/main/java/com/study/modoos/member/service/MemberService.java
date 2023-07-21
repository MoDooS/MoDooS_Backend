package com.study.modoos.member.service;

import com.study.modoos.common.exception.ErrorCode;
import com.study.modoos.common.exception.ModoosException;
import com.study.modoos.member.entity.Campus;
import com.study.modoos.member.entity.Department;
import com.study.modoos.member.entity.Member;
import com.study.modoos.member.repository.MemberRepository;
import com.study.modoos.member.request.ChangeMemberInfoRequest;
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
                .orElseThrow(() -> new ModoosException(ErrorCode.MEMBER_NOT_FOUND));
    }

    public void updateMemberInfo(Member currentMember, ChangeMemberInfoRequest memberInfoRequest) {
        currentMember.updateNickname(memberInfoRequest.getNickname());
        Campus campus = memberInfoRequest.getCampus();
        Department department = memberInfoRequest.getDepartment();

        if (!campus.hasDepartment(department))
            throw new ModoosException(ErrorCode.VALUE_NOT_IN_OPTION);
        currentMember.updateCampus(campus);
        currentMember.updateDepartment(department);
        memberRepository.save(currentMember);
    }

    public void join(Member member) {
        if (nicknameCheck(member.getNickname())) {
            throw new ModoosException(ErrorCode.ALREADY_MEMBER);
        }
        memberRepository.save(member);
    }

    public boolean nicknameCheck(String nickname) {
        return memberRepository.findByNickname(nickname).isPresent();
    }

    public boolean emailCheck(String email) {
        return memberRepository.findByEmail(email).isPresent();
    }

    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new ModoosException(ErrorCode.MEMBER_NOT_FOUND));
    }
}
