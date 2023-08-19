package com.study.modoos.alarm.entity;

import com.study.modoos.comment.entity.Comment;
import com.study.modoos.common.entity.BaseTimeEntity;
import com.study.modoos.member.entity.Member;
import com.study.modoos.study.entity.Study;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.bind.DefaultValue;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "Alarm")
public class Alarm extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private Member member;
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id", referencedColumnName = "id")
    private Study study;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", referencedColumnName = "id")
    private Comment comment;

    @Enumerated(EnumType.STRING)
    private AlarmType alarmType;
    private boolean isRead;

    @Builder
    public Alarm(Member member, Study study, Comment comment, String content, AlarmType alarmType) {
        this.member = member;
        this.study =study;
        this.comment = comment;
        this.content = content;
        this.alarmType = alarmType;
        this.isRead = false;
    }
}