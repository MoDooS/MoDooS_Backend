package com.study.modoos.participant.repository;

import com.study.modoos.member.entity.Member;
import com.study.modoos.participant.entity.Standby;
import com.study.modoos.study.entity.Study;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface StandbyRepository extends JpaRepository<Standby, Long> {
    boolean existsByStudyAndMember(Study study, Member currentUser);

    Standby findByMemberAndStudy(Member currentUser, Study study);

    @Query("SELECT s FROM Standby s JOIN FETCH s.member m JOIN FETCH s.study st WHERE s.id = :standbyId")
    Optional<Standby> findWithMemberAndStudyById(Long standbyId);
}

