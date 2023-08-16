package com.study.modoos.heart.entity;

import com.study.modoos.common.entity.BaseTimeEntity;
import com.study.modoos.member.entity.Member;
import com.study.modoos.study.entity.Study;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "heart")
public class Heart extends BaseTimeEntity {
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
    public Heart(Member member, Study study) {
        this.member = member;
        this.study =study;
    }
}
