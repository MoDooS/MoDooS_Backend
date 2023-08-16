package com.study.modoos.study.response;

import com.study.modoos.study.entity.Todo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CheckTodoResponse {

    private Long id;

    private String content;

    private boolean isCheck;


    public static CheckTodoResponse of(Todo todo, boolean isCheck) {
        return CheckTodoResponse.builder()
                .id(todo.getId())
                .content(todo.getContent())
                .isCheck(isCheck)
                .build();
    }

}
