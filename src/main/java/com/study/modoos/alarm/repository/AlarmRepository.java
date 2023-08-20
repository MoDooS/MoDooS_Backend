package com.study.modoos.alarm.repository;

import com.study.modoos.alarm.entity.Alarm;
import com.study.modoos.member.entity.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AlarmRepository extends JpaRepository<Alarm, Long> {
    Slice<Alarm> findByMemberOrderByCreatedAtDesc(Member member, Pageable sortedPageable);
}
