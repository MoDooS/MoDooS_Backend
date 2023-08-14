package com.study.modoos.feedback.entity;

import com.study.modoos.study.entity.Todo;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "feedbackTodo")
public class FeedbackTodo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "todoId", referencedColumnName = "id")
    private Todo todo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feedbackId", referencedColumnName = "id")
    private Feedback feedback;

    @Builder
    public FeedbackTodo(Todo todo, Feedback feedback) {
        this.todo = todo;
        this.feedback = feedback;
    }
}
