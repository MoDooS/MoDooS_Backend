package com.study.modoos.feedback.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.study.modoos.feedback.entity.Negative;
import com.study.modoos.feedback.entity.Positive;
import com.study.modoos.participant.entity.Participant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.study.modoos.feedback.entity.QFeedback.feedback;

@Repository
@RequiredArgsConstructor
public class FeedbackRepositoryImpl {
    private final JPAQueryFactory queryFactory;

    public long countPositiveFeedbackForParticipant(Participant participant, Positive positive) {
        return queryFactory.selectFrom(feedback)
                .where(feedback.receiver.eq(participant)
                        .and(feedback.positive.eq(positive)))
                .fetchCount();
    }

    public long countNegativeFeedbackForParticipant(Participant participant, Negative negative) {
        return queryFactory.selectFrom(feedback)
                .where(feedback.receiver.eq(participant)
                        .and(feedback.negative.eq(negative)))
                .fetchCount();
    }
}
