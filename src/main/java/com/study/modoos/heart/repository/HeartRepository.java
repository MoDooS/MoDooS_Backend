package com.study.modoos.heart.repository;


import com.study.modoos.heart.entity.Heart;
import com.study.modoos.member.entity.Member;
import com.study.modoos.study.entity.Study;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HeartRepository extends JpaRepository<Heart, Long> {
    Heart findByMemberAndStudy(Member currentUser, Study study);
}
