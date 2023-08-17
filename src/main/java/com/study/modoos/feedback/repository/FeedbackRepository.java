package com.study.modoos.feedback.repository;

import com.study.modoos.feedback.entity.Feedback;
import com.study.modoos.participant.entity.Participant;
import com.study.modoos.study.entity.Study;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    List<Feedback> findByReceiverAndStudyAndTimes(Participant receiver, Study study, int times);

    List<Feedback> findBySenderAndStudyAndTimes(Participant sender, Study study, int times);

    List<Feedback> findByReceiver(Participant receiver);
}
