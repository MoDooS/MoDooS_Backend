package com.study.modoos.study.repository;

import com.study.modoos.participant.entity.Participant;
import com.study.modoos.study.entity.Study;
import com.study.modoos.study.entity.StudyHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudyHistoryRepository extends JpaRepository<StudyHistory, Long> {

    List<StudyHistory> findStudyHistoryByStudyAndParticipant(Study study, Participant participant);

    Optional<StudyHistory> findStudyHistoryByStudyAndParticipantAndCurrentTurn(Study study, Participant participant, int current_turn);
}
