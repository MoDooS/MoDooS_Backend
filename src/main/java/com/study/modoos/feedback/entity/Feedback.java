package com.study.modoos.feedback.entity;


import com.study.modoos.common.entity.BaseTimeEntity;
import com.study.modoos.participant.entity.Participant;
import com.study.modoos.study.entity.Study;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
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
    
    @ColumnDefault("1")
    @Column(name = "participate")
    private int participate;  //참여도

    private boolean isReflected;    //해당 피드백이 점수에 반영되었는지 여부

    @Enumerated(EnumType.STRING)
    private Positive positive;

    @Enumerated(EnumType.STRING)
    private Negative negative;

    @Builder
    public Feedback(Study study, Participant receiver, Participant sender, int times,
                    int participate, Positive positive, Negative negative) {

        this.study = study;
        this.receiver = receiver;
        this.sender = sender;
        this.times = times;
        this.participate = participate;
        this.positive = positive;
        this.negative = negative;
        this.isReflected = false;
    }
}
