package com.study.modoos.heart.service;

import com.study.modoos.common.exception.ErrorCode;
import com.study.modoos.common.exception.ModoosException;
import com.study.modoos.heart.entity.Heart;
import com.study.modoos.heart.repository.HeartRepository;
import com.study.modoos.heart.repository.HeartRepositoryImpl;
import com.study.modoos.heart.response.HeartClickResponse;
import com.study.modoos.heart.response.HeartResponse;
import com.study.modoos.member.entity.Member;
import com.study.modoos.member.repository.MemberRepository;
import com.study.modoos.study.entity.Study;
import com.study.modoos.study.repository.StudyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class HeartService {
    private final HeartRepository heartRepository;
    private final HeartRepositoryImpl heartRepositoryImpl;
    private final MemberRepository memberRepository;
    private final StudyRepository studyRepository;


    public Slice<HeartResponse> getAllHeart(Member member, Pageable page) {

        return heartRepositoryImpl.getSliceOfHeartStudies(member, page);
    }

    public HeartClickResponse clickHeart(Member member, Long recruitId) {
        Member currentUser = memberRepository.findById(member.getId())
                .orElseThrow(() -> new ModoosException(ErrorCode.MEMBER_NOT_FOUND));
        Study study = studyRepository.findById(recruitId)
                .orElseThrow(() -> new ModoosException(ErrorCode.STUDY_NOT_FOUND));
        Heart existingHeart = heartRepository.findByMemberAndStudy(currentUser, study);


        if (existingHeart != null) {
            heartRepository.delete(existingHeart);
        }else {
            Heart heart = new Heart(currentUser, study);
            heartRepository.save(heart);
        }
        return HeartClickResponse.of(member, study, existingHeart == null);
    }
}
