package com.project.LMSSU.DTO;

import lombok.Builder;
import lombok.Data;

@Data
public class ToDoDTO {
    private Long toDoId;
    private String content;
    private Boolean isDone;

    @Builder
    public ToDoDTO(Long toDoId, String content, Boolean isDone) {
        this.toDoId = toDoId;
        this.content = content;
        this.isDone = isDone;
    }
}
