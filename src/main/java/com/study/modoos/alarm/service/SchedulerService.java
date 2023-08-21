package com.study.modoos.alarm.service;

import com.study.modoos.alarm.entity.Alarm;
import com.study.modoos.alarm.entity.AlarmType;
import com.study.modoos.alarm.repository.AlarmRepository;
import com.study.modoos.heart.repository.HeartRepository;
import com.study.modoos.member.entity.Member;
import com.study.modoos.study.entity.Study;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SchedulerService {
    private final AlarmRepository alarmRepository;
    private final HeartRepository heartRepository;

    @Scheduled(cron = "0 0 0 * * *")
    public void checkRecruitDeadline() {
        // 현재 날짜를 얻어온다.
        LocalDate currentDate = LocalDate.now();

        List<Study> heartStudies = heartRepository.findStudiesByRecruitDeadline(currentDate.plusDays(1));

        for (Study heartStudy : heartStudies) {
            createAlarmForStudy(heartStudy);
        }
    }

    private void createAlarmForStudy(Study heartStudy) {
        List<Member> memberList = heartRepository.findMembersByStudy(heartStudy);
        for (Member member: memberList) {
            Alarm alarm = new Alarm(member, heartStudy, null, String.format("회원님이 찜한 %s 스터디의 마감 하루 전입니다.", heartStudy.getTitle()), AlarmType.HEART_STUDY_DEADLINE);
            alarmRepository.save(alarm);
        }
    }
}