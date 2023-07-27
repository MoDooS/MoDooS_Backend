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

        Study study = request.createRecruit(currentMember);

        Study saved = studyRepository.save(study);

        return RecruitResponse.of(saved, true);
    }

    @Transactional
    public RecruitResponse changeRecruit(Member currentMember, Long recruitId, ChangeRecruitRequest request) {
        Study study = entityFinder.findStudy(recruitId);

        if (!currentMember.getId().equals(study.getLeader().getId())) {
            throw new ModoosException(ErrorCode.FORBIDDEN_ARTICLE);
        }

        //스터디가 이미 생성된 모집공고면 삭제 불가능
        if (study.getStatus() == 2) {
            throw new ModoosException(ErrorCode.STUDY_NOT_EDIT);
        }

        study.update(request.getCampus(), request.getChannel(), request.getCategory(),
                request.getExpected_start_at(), request.getExpected_end_at(), request.getContact(),
                request.getRule_content(), request.getTitle(), request.getDescription(),
                request.getAbsent(), request.getLate(), request.getOut(), request.getRule_content());


        studyRepository.save(study);
        return RecruitResponse.of(study, true);
    }

    @Transactional
    public void deleteRecruit(Member currentMember, Long recruitId) {
        Study study = entityFinder.findStudy(recruitId);

        //로그인한 유저와 글 작성자가 다르면 예외
        if (!currentMember.getId().equals(study.getLeader().getId())) {
            throw new ModoosException(ErrorCode.FORBIDDEN_ARTICLE);
        }

        //스터디가 이미 생성된 모집공고면 삭제 불가능
        if (study.getStatus() == 2) {
            throw new ModoosException(ErrorCode.STUDY_NOT_EDIT);
        }
        studyRepository.delete(study);
    }


}
