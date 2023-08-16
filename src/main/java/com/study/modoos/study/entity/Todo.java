package com.study.modoos.study.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "study_id", referencedColumnName = "id")
    private Study study;

    @Column(nullable = false, length = 100)
    private String content;

    @Builder
    public Todo(Study study, String content) {
        this.study = study;
        this.content = content;
    }


}
