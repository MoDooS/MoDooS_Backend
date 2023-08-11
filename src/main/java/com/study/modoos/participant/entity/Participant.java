package com.study.modoos.participant.entity;

import com.study.modoos.feedback.entity.Attendance;
import com.study.modoos.member.entity.Member;
import com.study.modoos.study.entity.Study;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Table(name = "participant")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = {"member", "study"})
public class Participant{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id", referencedColumnName = "id")
    private Study study;

    @Column
    private List<Attendance> attendanceList = new ArrayList<>();

    @ColumnDefault("0")
    @Column(name = "absent")
    private int absent;

    @ColumnDefault("0")
    @Column(name = "late")
    private int late;

    @ColumnDefault("0")
    @Column(name = "outs")
    private int out;

    @Builder
    public Participant(Member member, Study study) {
        this.member = member;
        this.study = study;
    }

    public void updateAbsent(int absent) {
        this.absent = absent;
    }

    public void updateLate(int late) {
        this.late = late;
    }

    public void updateOut(int out) {
        this.out = out;
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
