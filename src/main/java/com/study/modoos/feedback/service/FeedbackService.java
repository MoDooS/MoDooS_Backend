package com.study.modoos.feedback.service;

import com.study.modoos.feedback.entity.Negative;
import com.study.modoos.feedback.entity.Positive;
import com.study.modoos.feedback.repository.FeedbackRepository;
import com.study.modoos.feedback.repository.FeedbackRepositoryImpl;
import com.study.modoos.feedback.response.MyFeedbackResponse;
import com.study.modoos.member.entity.Member;
import com.study.modoos.participant.entity.Participant;
import com.study.modoos.participant.repository.ParticipantRepository;
import com.study.modoos.study.response.NegativeKeywordResponse;
import com.study.modoos.study.response.PositiveKeywordResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class FeedbackService {
    private final FeedbackRepository feedbackRepository;

    private final FeedbackRepositoryImpl feedbackRepositoryImpl;

    private final ParticipantRepository participantRepository;

    public MyFeedbackResponse getMyFeedbacks(Member currentMember) {
        //현재 유저의 참여 리스트
        List<Participant> participantList = participantRepository.findByMember(currentMember);

        //해시맵 사용
        HashMap<Positive, Long> positiveMap = new HashMap<>();
        HashMap<Negative, Long> negativeMap = new HashMap<>();

        for (Positive positive : Positive.values()) {
            positiveMap.put(positive, 0L);
        }

        for (Negative negative : Negative.values()) {
            negativeMap.put(negative, 0L);
        }

        for (Participant participant : participantList) {
            for (Positive positive : Positive.values()) {
                long count = feedbackRepositoryImpl.countPositiveFeedbackForParticipant(participant, positive);

                positiveMap.put(positive, positiveMap.get(positive) + count);
            }

            for (Negative negative : Negative.values()) {
                long count = feedbackRepositoryImpl.countNegativeFeedbackForParticipant(participant, negative);

                negativeMap.put(negative, negativeMap.get(negative) + count);
            }
        }


        List<PositiveKeywordResponse> positiveList = new ArrayList<>();
        List<NegativeKeywordResponse> negativeList = new ArrayList<>();


        for (Positive positive : Positive.values()) {
            long count = positiveMap.get(positive);

            PositiveKeywordResponse response = PositiveKeywordResponse.builder()
                    .count(count)
                    .positive(positive)
                    .build();
            positiveList.add(response);
        }

        for (Negative negative : Negative.values()) {
            long count = negativeMap.get(negative);

            NegativeKeywordResponse response = NegativeKeywordResponse.builder()
                    .count(count)
                    .negative(negative)
                    .build();
            negativeList.add(response);
        }

        return MyFeedbackResponse.builder()
                .positiveList(positiveList)
                .negativeList(negativeList)
                .build();
    }
}
