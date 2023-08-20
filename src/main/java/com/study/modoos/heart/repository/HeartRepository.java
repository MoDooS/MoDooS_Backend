package com.study.modoos.heart.repository;


import com.study.modoos.heart.entity.Heart;
import com.study.modoos.member.entity.Member;
import com.study.modoos.study.entity.Study;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

import java.util.List;

public interface HeartRepository extends JpaRepository<Heart, Long> {
    Heart findByMemberAndStudy(Member currentUser, Study study);


    @Query("SELECT h.study FROM Heart h WHERE h.study.status = 'RECRUITING' AND h.study.recruit_deadline = ?1")
    List<Study> findStudiesByRecruitDeadline(LocalDate recruitDeadline);

    @Query("SELECT h.member FROM Heart h WHERE h.study = ?1")
    List<Member> findMembersByStudy(Study study);

    List<Heart> findByMember(Member member);

}
