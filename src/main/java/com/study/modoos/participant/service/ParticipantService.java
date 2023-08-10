package com.study.modoos.participant.service;

import com.study.modoos.common.exception.ErrorCode;
import com.study.modoos.common.exception.ModoosException;
import com.study.modoos.member.entity.Member;
import com.study.modoos.member.repository.MemberRepository;
import com.study.modoos.participant.entity.Participant;
import com.study.modoos.participant.entity.Standby;
import com.study.modoos.participant.repository.ParticipantRepository;
import com.study.modoos.participant.repository.StandbyRepository;
import com.study.modoos.participant.repository.StandbyRepositoryImpl;
import com.study.modoos.participant.response.AllApplicationResponse;
import com.study.modoos.participant.response.ParticipantResponse;
import com.study.modoos.participant.response.StandbyResponse;
import com.study.modoos.study.entity.Study;
import com.study.modoos.study.repository.StudyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ParticipantService {

    private final ParticipantRepository participantRepository;
    private final StandbyRepository standbyRepository;
    private final MemberRepository memberRepository;
    private final StudyRepository studyRepository;
    private final StandbyRepositoryImpl standbyRepositoryImpl;

    public Slice<AllApplicationResponse> getAllApply(Member member, Pageable page) {
        return standbyRepositoryImpl.getSliceOfAllApplication(member, page);
    }

    public StandbyResponse applyStudy(Member member, Long studyId) {
        Member currentUser = memberRepository.findById(member.getId())
                .orElseThrow(() -> new ModoosException(ErrorCode.MEMBER_NOT_FOUND));
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new ModoosException(ErrorCode.STUDY_NOT_FOUND));

        boolean isParticipant = participantRepository.existsByStudyAndMember(study, currentUser);
        boolean isStandby = standbyRepository.existsByStudyAndMember(study, currentUser);

        if (isParticipant) {
            throw new ModoosException(ErrorCode.ALREADY_PARTICIPANT);
        }
        if(isStandby){
            throw new ModoosException(ErrorCode.ALREADY_STANDBY);
        }
        Standby standby = new Standby(currentUser, study);
        standbyRepository.save(standby);
        Standby standby_waiter = standbyRepository.findByMemberAndStudy(currentUser, study);
        return StandbyResponse.of(standby_waiter);
    }

    public ParticipantResponse acceptApplication(Member member, Long standbyId) {
        Member currentUser = memberRepository.findById(member.getId())
                .orElseThrow(() -> new ModoosException(ErrorCode.MEMBER_NOT_FOUND));
        Standby standby = standbyRepository.findWithMemberAndStudyById(standbyId)
                .orElseThrow(() -> new ModoosException(ErrorCode.WAITER_NOT_FOUND));

        Study study = standby.getStudy();
        Member applicant  = standby.getMember();

        if (!study.getLeader().equals(currentUser)){
            throw new ModoosException(ErrorCode.FORBIDDEN_STUDY_ACCEPT);
        }

        Participant participant = new Participant(applicant, study);
        participantRepository.save(participant);
        Participant participant_confirm = participantRepository.findByMemberAndStudy(applicant, study);
        standbyRepository.delete(standby);

        return ParticipantResponse.of(participant_confirm);
    }

    public void rejectApplication(Member member, Long standbyId) {
        Member currentUser = memberRepository.findById(member.getId())
                .orElseThrow(() -> new ModoosException(ErrorCode.MEMBER_NOT_FOUND));
        Standby standby = standbyRepository.findWithMemberAndStudyById(standbyId)
                .orElseThrow(() -> new ModoosException(ErrorCode.WAITER_NOT_FOUND));

        Study study = standby.getStudy();
        if (!study.getLeader().equals(currentUser)){
            throw new ModoosException(ErrorCode.FORBIDDEN_STUDY_ACCEPT);
        }

        standbyRepository.delete(standby);
    }
}
