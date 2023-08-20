package com.study.modoos.participant.repository;

import com.study.modoos.member.entity.Member;
import com.study.modoos.participant.entity.Participant;
import com.study.modoos.study.entity.Study;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    boolean existsByStudyAndMember(Study study, Member currentUser);
    int countByStudy(Study study);
    Optional<Participant> findByMemberAndStudy(Member member, Study study);
    List<Participant> findByStudy(Study study);
}
