package com.study.modoos.study.entity;

import com.study.modoos.member.entity.Campus;
import com.study.modoos.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "study")
public class Study {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "leader_id")
    private Member leader;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 2000)
    private String description;

    @ColumnDefault("0")
    @Column(name = "recruits_count")
    private int recruits_count;

    @ColumnDefault("0")
    @Column(name = "participants_count")
    private int participants_count;

    @ColumnDefault("0")
    @Column(name = "status")
    private int status;

    @Column(nullable = false, updatable = false)
    private LocalDate recruit_deadline;

    @ColumnDefault("FALSE")
    @Column(nullable = false)
    private Boolean isOnline;

    @Column(nullable = false, updatable = false)
    private LocalDate expected_start_at;

    @Column(nullable = false, updatable = false)
    private LocalDate expected_end_at;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Enumerated(EnumType.STRING)
    private Campus campus;

    @Column(updatable = false)
    private LocalDate start_at;

    @Column(updatable = false)
    private LocalDate end_at;

    @ColumnDefault("0")
    @Column(name = "period")
    private int period;

    @ColumnDefault("0")
    @Column(name = "total_turn")
    private int total_turn;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        else if (!(obj instanceof Study)) return false;
        Study study = (Study) obj;
        return Objects.equals(id, study.getId());
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
