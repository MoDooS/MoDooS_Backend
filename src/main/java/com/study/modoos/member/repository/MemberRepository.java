package com.study.modoos.member.repository;

import java.util.Optional;

import com.study.modoos.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByNickname(String nickname);
    Optional<Member> findByEmail(String email);
}
