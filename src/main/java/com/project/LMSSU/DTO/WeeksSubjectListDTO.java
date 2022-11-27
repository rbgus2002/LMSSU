package com.project.LMSSU.DTO;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class WeeksSubjectListDTO {
    private LocalDate startDate;
    private LocalDate endDate;
    private List subjectDTO;

    @Builder
    public WeeksSubjectListDTO(LocalDate startDate, LocalDate endDate, List subjectDTO) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.subjectDTO = subjectDTO;
    }
}
