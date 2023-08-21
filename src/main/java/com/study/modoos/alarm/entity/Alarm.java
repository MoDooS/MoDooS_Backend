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
    public Alarm(Member member, Study study, Comment comment, String content,AlarmType alarmType) {
        this.member = member;
        this.study = study;
        this.comment = comment;
        this.content = content;
        this.alarmType = alarmType;
        this.isRead = false;
    }

    // comment 필드가 null인 경우를 고려하여 commentId 반환 메서드 추가
    public Long getCommentId(Alarm alarm) {
        return alarm.comment != null ? alarm.comment.getId() : null;
    }

    public void readAlarm(boolean isRead){
        this.isRead = isRead;
    }
}