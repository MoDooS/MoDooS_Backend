package com.study.modoos.alarm.service;

import com.study.modoos.alarm.entity.Alarm;
import com.study.modoos.alarm.entity.AlarmType;
import com.study.modoos.alarm.repository.AlarmRepository;
import com.study.modoos.alarm.response.AlarmResponse;
import com.study.modoos.alarm.response.ReadAlarmResponse;
import com.study.modoos.common.exception.ErrorCode;
import com.study.modoos.common.exception.ModoosException;
import com.study.modoos.member.entity.Member;
import com.study.modoos.member.repository.MemberRepository;
import com.study.modoos.participant.repository.ParticipantRepository;
import com.study.modoos.study.entity.Study;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class AlarmService {
    private final AlarmRepository alarmRepository;
    private final MemberRepository memberRepository;
    private final ParticipantRepository participantRepository;

    public Slice<AlarmResponse> getAlarm(Member currentUser, Pageable pageable) {
        Member member = memberRepository.findByEmail(currentUser.getEmail())
                .orElseThrow(() -> new ModoosException(ErrorCode.MEMBER_NOT_FOUND));

        //해당 회원의 진행중인 스터디의 해당 주차에 피드백 시작인 경우, 알림 추가
        checkFeedbackStart(member);
        //해당 회원의 진행중인 스터디의 해당 주차의 피드백 마감 2시간 전인 경우, 알림 추가
        checkFeedbackTimeEnd(member);

        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "createdAt"));

        Slice<Alarm> alarmSlice = alarmRepository.findByMemberOrderByCreatedAtDesc(member, sortedPageable);

        List<AlarmResponse> alarmResponses = alarmSlice.getContent().stream()
                .map(AlarmResponse::of)
                .collect(Collectors.toList());

        return new SliceImpl<>(alarmResponses, sortedPageable, alarmSlice.hasNext());

    }
    private void checkFeedbackStart(Member member) {
        List<Study> studyList = participantRepository.findOngoingStudyByMember(member);
        if (studyList.size() > 0){
            for (Study study:studyList) {
                Optional<Alarm> existedAlarm = alarmRepository.findByStudyAndMemberAndAlarmType(study, member, AlarmType.FEEDBACK_START);
                if (existedAlarm.isPresent()){
                    if (isDateInCurrentStudyTurn(existedAlarm.get().getCreatedAt().toLocalDate(), study.getStart_at(),study.getPeriod())){
                        break;
                    }
                }

                if (LocalDateTime.now().isAfter(study.getEnd_at().atTime(study.getEndTime()))){
                    Alarm alarm = new Alarm(member, study, null, String.format("%s 스터디의 %s 주차 피드백 시간이 되었습니다.", study.getTitle(), calculateStudyTurn(study.getStart_at(),study.getPeriod())), AlarmType.FEEDBACK_START);
                    alarmRepository.save(alarm);
                }
            }
        }
    }

    private void checkFeedbackTimeEnd(Member member) {
        List<Study> studyList = participantRepository.findOngoingStudyByMember(member);
        if(studyList.size() > 0){
            for (Study study:studyList){
                Optional<Alarm> existedAlarm = alarmRepository.findByStudyAndMemberAndAlarmType(study, member, AlarmType.FEEDBACK_END);
                if (existedAlarm.isPresent()){
                    if (isDateInCurrentStudyTurn(existedAlarm.get().getCreatedAt().toLocalDate(), study.getStart_at(),study.getPeriod())){
                        break;
                    }
                }
                if (isTodayStudyDay(study.getStart_at()) && isFeedbackPeriodEndingIn2Hours(study.getEndTime())){
                    Alarm alarm = new Alarm(member, study, null, String.format("%s 스터디의 %s 주차 피드백 시간이 2시간 남았습니다.", study.getTitle(), calculateStudyTurn(study.getStart_at(),study.getPeriod())), AlarmType.FEEDBACK_END);
                    alarmRepository.save(alarm);
                }
            }
        }
    }
    public static int calculateStudyTurn(LocalDate startAt, int period) {
        long weeksBetween = ChronoUnit.WEEKS.between(startAt, LocalDateTime.now());
        int studyTurn = (int) Math.ceil((double) weeksBetween / period);
        return studyTurn + 1;
    }

    //주어진 날짜가 현재 스터디 회차 기간에 속하는지 확인하는 메서드
    public boolean isDateInCurrentStudyTurn(LocalDate date, LocalDate startAt, int period) {
        int currentStudyTurn = calculateStudyTurn(startAt, period);
        LocalDate currentTurnStartDate = startAt.plusWeeks((currentStudyTurn - 1) * period);
        LocalDate currentTurnEndDate = currentTurnStartDate.plusWeeks(period);

        // 특정 날짜가 현재 회차의 기간에 포함되는지 확인
        return !date.isBefore(currentTurnStartDate) && !date.isAfter(currentTurnEndDate);
    }

    // 현재로부터 2시간 후에 피드백 기간이 끝나는지 확인하는 메서드
    public static boolean isFeedbackPeriodEndingIn2Hours(LocalTime endTime) {
        LocalTime feedbackEndTime = endTime.plusHours(24);

        return feedbackEndTime.isBefore(LocalTime.now().plusHours(2));
    }

    // 오늘이 스터디 하는 날인지 확인하는 메서드
    public static boolean isTodayStudyDay(LocalDate studyStart) {
        DayOfWeek studyDay = studyStart.getDayOfWeek();
        DayOfWeek currentDay = LocalDateTime.now().getDayOfWeek();

        return currentDay == studyDay;
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

    public void readAllAlarm(Member currentUser) {
        Member member = memberRepository.findByEmail(currentUser.getEmail())
                .orElseThrow(() -> new ModoosException(ErrorCode.MEMBER_NOT_FOUND));

        List<Alarm> unreadAlarms = alarmRepository.findByMemberAndIsRead(member, false);
        if (unreadAlarms.isEmpty()){
            throw new ModoosException(ErrorCode.MEMBER_HAS_NOT_ALARM);
        }else {
            for (Alarm alarm : unreadAlarms) {
                alarm.readAlarm(true);
                alarmRepository.save(alarm);
            }
        }
    }
}
