package com.project.LMSSU.DTO;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class IncompleteContentsResponseDTO {
    private String subjectName;
    private String professorName;
    private String link;
    private List<SubjectContents_endDateDTO> subjectContents_endDateDTOList;

    @Builder
    public IncompleteContentsResponseDTO(String subjectName, String professorName, String link, List<SubjectContents_endDateDTO> subjectContents_endDateDTOList) {
        this.subjectName = subjectName;
        this.professorName = professorName;
        this.link = link;
        this.subjectContents_endDateDTOList = subjectContents_endDateDTOList;
    }
}
