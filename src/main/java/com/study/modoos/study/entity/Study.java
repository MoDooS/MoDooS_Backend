package com.study.modoos.study.entity;

import com.study.modoos.comment.entity.Comment;
import com.study.modoos.common.entity.BaseTimeEntity;
import com.study.modoos.member.entity.Campus;
import com.study.modoos.member.entity.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "study")
public class Study extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "leader_id", referencedColumnName = "id")
    private Member leader;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(length = 2000)
    private String description;

    @ColumnDefault("3")
    @Min(3)
    @Column(name = "recruits_count")
    private int recruits_count;

    @ColumnDefault("1")
    @Min(1)
    @Column(name = "participants_count")
    private int participants_count;

    //0이면 모집중, 1이면 모집완료, 2면 스터디 생성 완료
    @ColumnDefault("0")
    @Column(name = "status")
    private int status;

    @Column(nullable = false)
    private LocalDate recruit_deadline;

    @Enumerated(EnumType.STRING)
    private Channel channel;

    @Column(nullable = false, updatable = false)
    private LocalDate expected_start_at;

    @Column(nullable = false, updatable = false)
    private LocalDate expected_end_at;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Enumerated(EnumType.STRING)
    private Campus campus;

    @Column(name = "contact")
    private String contact;

    @Column(name = "link")
    private String link;

    @ColumnDefault("0")
    @Column(name = "absent")
    private int absent;

    @ColumnDefault("0")
    @Column(name = "late")
    private int late;

    @ColumnDefault("0")
    @Column(name = "outs")
    private int out;

    @Column
    private LocalDate start_at;

    @Column
    private LocalDate end_at;

    @Column
    private LocalTime studyTime;

    @Column
    private LocalTime endTime;

    @ColumnDefault("0")
    @Column(name = "period")
    private int period;

    @ColumnDefault("0")
    @Column(name = "current_turn")
    private int current_turn;

    @ColumnDefault("0")
    @Column(name = "total_turn")
    private int total_turn;

    @OneToMany(mappedBy = "study", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @OrderBy("id asc")
    private List<Comment> comments;

    @Builder
    public Study(Member leader, String title, String description, int recruits_count,
                 LocalDate recruit_deadline, Channel channel, LocalDate expected_start_at,
                 LocalDate expected_end_at, Category category, Campus campus, String contact,
                 String link, int absent, int late, int out) {
        this.leader = leader;
        this.title = title;
        this.description = description;
        this.recruits_count = recruits_count;
        this.participants_count = 1;
        this.recruit_deadline = recruit_deadline;
        this.channel = channel;
        this.expected_start_at = expected_start_at;
        this.expected_end_at = expected_end_at;
        this.category = category;
        this.campus = campus;
        this.contact = contact;
        this.link = link;
        this.absent = absent;
        this.late = late;
        this.out = out;
        this.status = 0;
    }

    public void update(Campus campus, Channel channel, Category category, LocalDate expected_start_at,
                       LocalDate expected_end_at, String contact, String link, String title, String description,
                       int absent, int late, int out) {
        this.title = title;
        this.description = description;
        this.channel = channel;
        this.expected_start_at = expected_start_at;
        this.expected_end_at = expected_end_at;
        this.category = category;
        this.campus = campus;
        this.contact = contact;
        this.link = link;
        this.absent = absent;
        this.late = late;
        this.out = out;
    }

    public boolean isWrittenPost(Member member) {
        return this.leader.getId().equals(member.getId());
    }

    /*
     * 나중에 여기에 스터디 생성 시 진짜 시작일, 종료일, 회차, 기간 update하는 로직 넣기(생성자 새로 만들지 x)
     */

    public void setStudy(LocalDate start_at, LocalDate end_at, LocalTime studyTime, LocalTime endTime,
                         int period, int total_turn, int absent, int late, int out) {
        this.start_at = start_at;
        this.end_at = end_at;
        this.studyTime = studyTime;
        this.endTime = endTime;
        this.period = period;
        this.total_turn = total_turn;
        this.absent = absent;
        this.late = late;
        this.out = out;
        this.current_turn = 0;
    }

    public void updateStatus(int status) {
        this.status = status;
    }

    public void updateCurrentTurn(int current_turn) {
        this.current_turn = current_turn;
    }

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
