package com.study.modoos.study.entity;

import com.study.modoos.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@Getter
@Table(name = "participant")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = {"member", "study"})
public class Participant {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id", referencedColumnName = "id")
    private Study study;

    @Builder
    public Participant(Member member, Study study) {
        this.member = member;
        this.study = study;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        else if (!(obj instanceof Participant)) return false;
        Participant participant = (Participant) obj;
        return Objects.equals(id, participant.getId());
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

}
