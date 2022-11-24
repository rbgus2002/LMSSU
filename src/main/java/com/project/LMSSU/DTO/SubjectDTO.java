package com.project.LMSSU.DTO;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class SubjectDTO {
    private String title;
    private List subjectContentsTitle;
    private List toDoDTO;

    @Builder
    public SubjectDTO(String title, List subjectContentsTitle, List toDoDTO) {
        this.title = title;
        this.subjectContentsTitle = subjectContentsTitle;
        this.toDoDTO = toDoDTO;
    }
}
