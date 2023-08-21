package com.study.modoos.study.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "studyHistoryTodo")
public class StudyHistoryTodo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "todoId", referencedColumnName = "id")
    private Todo todo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "studyHistoryId", referencedColumnName = "id")
    private StudyHistory studyHistory;

    @Builder
    public StudyHistoryTodo(Todo todo, StudyHistory studyHistory) {
        this.todo = todo;
        this.studyHistory = studyHistory;
    }
}
