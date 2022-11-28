package com.project.LMSSU.DTO;

import lombok.Builder;
import lombok.Data;

@Data
public class ToDoRequestDTO {
    Long studentId;
    Long subjectId;
    int week;
    String content;
}
