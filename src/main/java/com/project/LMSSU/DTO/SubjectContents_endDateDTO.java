package com.project.LMSSU.DTO;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
public class SubjectContents_endDateDTO {
    private String title;
    private String contentsType;
    private LocalDate endDate;

    @Builder

    public SubjectContents_endDateDTO(String title, String contentsType, LocalDate endDate) {
        this.title = title;
        this.contentsType = contentsType;
        this.endDate = endDate;
    }
}
