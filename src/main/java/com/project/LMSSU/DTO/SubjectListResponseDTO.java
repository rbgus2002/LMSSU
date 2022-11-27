package com.project.LMSSU.DTO;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class SubjectListResponseDTO {
    private WeeksSubjectListDTO weeksSubjectListDTO;
    private List subjectNoticeListDTO;

    @Builder
    public SubjectListResponseDTO(WeeksSubjectListDTO weeksSubjectListDTO, List subjectNoticeListDTO) {
        this.weeksSubjectListDTO = weeksSubjectListDTO;
        this.subjectNoticeListDTO = subjectNoticeListDTO;
    }
}
