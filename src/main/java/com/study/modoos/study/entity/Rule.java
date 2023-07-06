package com.study.modoos.study.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "rule")
public class Rule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "study_id")
    private Study study;

    @Column(nullable = false, length = 2000)
    private String content;

    @ColumnDefault("0")
    @Column(name = "absent")
    private int absent;

    @ColumnDefault("0")
    @Column(name = "late")
    private int late;

    @ColumnDefault("0")
    @Column(name = "out")
    private int out;

    @Builder
    public Rule(Study study, String content, int absent, int late, int out) {
        this.study = study;
        this.content = content;
        this.absent = absent;
        this.late = late;
        this.out = out;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        else if (!(obj instanceof Rule)) return false;
        Rule rule = (Rule) obj;
        return Objects.equals(id, rule.getId());
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
