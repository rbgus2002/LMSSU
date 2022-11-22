package com.project.LMSSU.DTO;

import com.project.LMSSU.Entity.Subject;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class IncompleteTaskResponseDTO {
    private Long subjectId;
    private String subjectName;
    private List subjectContents;

    @Builder
    public IncompleteTaskResponseDTO(Subject subject, List subjectContents) {
        this.subjectId = subject.getId();
        this.subjectName = subject.getSubjectName();
        this.subjectContents = subjectContents;
    }
}
