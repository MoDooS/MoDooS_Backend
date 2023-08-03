package com.study.modoos.participant.entity;

import com.study.modoos.member.entity.Member;
import com.study.modoos.study.entity.Study;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Table(name = "standby")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = {"member", "study"})
public class Standby {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id", referencedColumnName = "id")
    private Study study;

    @Builder
    public Standby(Member member, Study study){
        this.member = member;
        this.study = study;
    }
}
