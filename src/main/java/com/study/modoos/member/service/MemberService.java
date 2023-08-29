package com.study.modoos.member.service;

import com.study.modoos.common.exception.ErrorCode;
import com.study.modoos.common.exception.ModoosException;
import com.study.modoos.heart.service.HeartService;
import com.study.modoos.member.entity.Campus;
import com.study.modoos.member.entity.Department;
import com.study.modoos.member.entity.Member;
import com.study.modoos.member.repository.MemberRepository;
import com.study.modoos.member.request.ChangeMemberInfoRequest;
import com.study.modoos.member.response.MemberProfileResponse;
import com.study.modoos.member.response.MyInfoResponse;
import com.study.modoos.study.entity.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    private final HeartService heartService;

    public MyInfoResponse getMyInfo(Member currentMember) {
        if (currentMember == null) {
            throw new ModoosException(ErrorCode.INVALID_TOKEN);
        }
        return memberRepository.findById(currentMember.getId())
                .map(MyInfoResponse::of)
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

    //랭킹, 점수, 스터디 태그, 스터디 개수, 자기자신 여부 넘겨주기(프로필)
    public MemberProfileResponse getMemberProfile(Member currentMember, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ModoosException(ErrorCode.MEMBER_NOT_FOUND));


        //관심있게 보는 스터디 태그
        List<Category> categoryList = heartService.findMostCommonCategoryForMember(member);

        //사용자가 관심있게 본 스터디 개수
        int heartCount = heartService.countHeartStudy(member);

        boolean isSelf = member.getId().equals(currentMember.getId()) ? true : false;

        return MemberProfileResponse.of(member, categoryList, heartCount, isSelf);


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
