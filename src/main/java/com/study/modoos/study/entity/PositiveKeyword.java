package com.study.modoos.study.entity;

import com.study.modoos.feedback.entity.Positive;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "positiveKeyword")
public class PositiveKeyword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Positive positive;

    @ColumnDefault("0")
    @Column(name = "positive_count")
    private int count;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "studyHistory_id", referencedColumnName = "id")
    private StudyHistory studyHistory; // 연관 관계 필드명 변경

    @Builder
    public PositiveKeyword(Positive positive, int count, StudyHistory studyHistory) {
        this.positive = positive;
        this.count = count;
        this.studyHistory = studyHistory;
    }
}
