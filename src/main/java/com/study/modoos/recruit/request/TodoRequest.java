package com.study.modoos.recruit.request;

import com.study.modoos.study.entity.Study;
import com.study.modoos.study.entity.Todo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TodoRequest {
    Long id;

    String content;

    String requestType;

    public Todo createTodo(Study study) {
        return Todo.builder()
                .study(study)
                .content(content)
                .build();
    }
}
