package com.study.modoos.alarm.repository;

import com.study.modoos.alarm.entity.Alarm;
import com.study.modoos.alarm.entity.AlarmType;
import com.study.modoos.member.entity.Member;
import com.study.modoos.study.entity.Study;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface AlarmRepository extends JpaRepository<Alarm, Long> {
    Slice<Alarm> findByMemberOrderByCreatedAtDesc(Member member, Pageable sortedPageable);
    Optional<Alarm> findByStudyAndMemberAndAlarmType(Study study, Member member, AlarmType alarmType);
}
