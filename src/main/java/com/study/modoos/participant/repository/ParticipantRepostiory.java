package com.study.modoos.participant.repository;

import com.study.modoos.member.entity.Member;
import com.study.modoos.participant.entity.Participant;
import com.study.modoos.study.entity.Study;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipantRepostiory extends JpaRepository<Participant, Long> {
    boolean existsByStudyAndMember(Study study, Member currentUser);
}
