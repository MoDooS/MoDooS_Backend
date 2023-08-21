package com.study.modoos.study.entity;

import com.study.modoos.participant.entity.Participant;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "studyHistory")
public class StudyHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ColumnDefault("0")
    @Column(name = "current_turn")
    private int currentTurn;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "study_id", referencedColumnName = "id")
    private Study study;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "participant_id", referencedColumnName = "id")
    private Participant participant;

    @ColumnDefault("1")
    @Column(name = "participate")
    private int participate;  //참여도

    @OneToMany(mappedBy = "studyHistory", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<PositiveKeyword> positiveKeywords;

    @OneToMany(mappedBy = "studyHistory", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<NegativeKeyword> negativeKeywords;

    @Builder
    public StudyHistory(int current_turn, Study study, Participant participant,
                        int participate, List<PositiveKeyword> positiveKeywords, List<NegativeKeyword> negativeKeywords) {
        this.currentTurn = current_turn;
        this.study = study;
        this.participant = participant;
        this.participate = participate;
        this.positiveKeywords = positiveKeywords;
        this.negativeKeywords = negativeKeywords;
    }

}
