package com.study.modoos.feedback.entity;


import com.study.modoos.common.entity.BaseTimeEntity;
import com.study.modoos.study.entity.Participant;
import com.study.modoos.study.entity.Study;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "feedback")
public class Feedback extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "study_id", referencedColumnName = "id")
    private Study study;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "receiver_id", referencedColumnName = "id")
    private Participant receiver;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "sender_id", referencedColumnName = "id")
    private Participant sender;

    @ColumnDefault("0")
    @Column(name = "times")
    private int times;  //회차번호

    @ColumnDefault("0")
    @Column(name = "attend")
    private int attend; //출석여부

    @ColumnDefault("0")
    @Column(name = "deligence")
    private int deligence;  //규칙이행도

    @ColumnDefault("0")
    @Column(name = "participate")
    private int participate;  //참여도

    @Enumerated(EnumType.STRING)
    private Positive positive;

    @Enumerated(EnumType.STRING)
    private Negative negative;
}
