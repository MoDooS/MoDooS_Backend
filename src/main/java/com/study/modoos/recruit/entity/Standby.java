package com.study.modoos.recruit.entity;

import com.study.modoos.member.entity.Member;
import com.study.modoos.study.entity.Study;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@Table(name = "standby")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = {"member", "study"})
public class Standby {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id", referencedColumnName = "id")
    private Study study;
}
