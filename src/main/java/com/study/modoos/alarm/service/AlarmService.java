package com.study.modoos.alarm.service;

import com.study.modoos.alarm.entity.Alarm;
import com.study.modoos.alarm.repository.AlarmRepository;
import com.study.modoos.alarm.response.AlarmResponse;
import com.study.modoos.alarm.response.ReadAlarmResponse;
import com.study.modoos.common.exception.ErrorCode;
import com.study.modoos.common.exception.ModoosException;
import com.study.modoos.member.entity.Member;
import com.study.modoos.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class AlarmService {
    private final AlarmRepository alarmRepository;
    private final MemberRepository memberRepository;

    public Slice<AlarmResponse> getAlarm(Member currentUser, Pageable pageable) {
        Member member = memberRepository.findByEmail(currentUser.getEmail())
                .orElseThrow(() -> new ModoosException(ErrorCode.MEMBER_NOT_FOUND));

        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "createdAt"));

        Slice<Alarm> alarmSlice = alarmRepository.findByMemberOrderByCreatedAtDesc(member, sortedPageable);

        List<AlarmResponse> alarmResponses = alarmSlice.getContent().stream()
                .map(AlarmResponse::of)
                .collect(Collectors.toList());

        return new SliceImpl<>(alarmResponses, sortedPageable, alarmSlice.hasNext());

    }

    public ReadAlarmResponse readAlarm(Member currentUser, Long alarmId) {
        Member member = memberRepository.findByEmail(currentUser.getEmail())
                .orElseThrow(() -> new ModoosException(ErrorCode.MEMBER_NOT_FOUND));

        Alarm requestAlarm = alarmRepository.findById(alarmId)
                .orElseThrow(() -> new ModoosException(ErrorCode.ALARM_NOT_FOUND));
        requestAlarm.readAlarm(true);
        alarmRepository.save(requestAlarm);
        return ReadAlarmResponse.readAlarm(requestAlarm, true);
    }
}
