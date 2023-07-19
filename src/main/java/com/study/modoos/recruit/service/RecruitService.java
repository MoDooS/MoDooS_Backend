package com.study.modoos.recruit.service;

import com.study.modoos.common.exception.ErrorCode;
import com.study.modoos.common.exception.ModoosException;
import com.study.modoos.common.service.EntityFinder;
import com.study.modoos.member.entity.Member;
import com.study.modoos.member.repository.MemberRepository;
import com.study.modoos.recruit.request.ChangeRecruitRequest;
import com.study.modoos.recruit.request.RecruitRequest;
import com.study.modoos.recruit.response.RecruitResponse;
import com.study.modoos.study.entity.Study;
import com.study.modoos.study.repository.StudyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class RecruitService {
    private final MemberRepository memberRepository;
    private final StudyRepository studyRepository;
    private final EntityFinder entityFinder;

    @Transactional
    public RecruitResponse postRecruit(Member currentMember, RecruitRequest request) {

        Study study = Study.builder()
                .leader(currentMember)
                .title(request.getTitle())
                .description(request.getDescription())
                .recruits_count(request.getRecruits_count())
                .recruit_deadline(request.getRecruit_deadline())
                .channel(request.getChannel())
                .expected_start_at(request.getExpected_start_at())
                .expected_end_at(request.getExpected_end_at())
                .category(request.getCategory())
                .campus(request.getCampus())
                .contact(request.getContact())
                .link(request.getLink())
                .late(request.getLate())
                .absent(request.getAbsent())
                .out(request.getOut())
                .rule_content(request.getRule_content())
                .build();

        Study saved = studyRepository.save(study);

        return RecruitResponse.of(saved, true);
    }

    @Transactional
    public RecruitResponse changeRecruit(Member currentMember, Long recruitId, ChangeRecruitRequest request) {
        Study study = entityFinder.findStudy(recruitId);

        if (!currentMember.getId().equals(study.getLeader().getId())) {
            throw new ModoosException(ErrorCode.FORBIDDEN_ARTICLE);
        }

        study.update(request.getCampus(), request.getChannel(), request.getCategory(),
                request.getExpected_start_at(), request.getExpected_end_at(), request.getContact(),
                request.getRule_content(), request.getTitle(), request.getDescription(),
                request.getAbsent(), request.getLate(), request.getOut(), request.getRule_content());

        return RecruitResponse.of(study, true);
    }

    @Transactional
    public void deleteRecruit(Member currentMember, Long recruitId) {
        Study study = entityFinder.findStudy(recruitId);
        if (!currentMember.getId().equals(study.getLeader().getId())) {
            throw new ModoosException(ErrorCode.FORBIDDEN_ARTICLE);
        }

        studyRepository.delete(study);
    }
}
