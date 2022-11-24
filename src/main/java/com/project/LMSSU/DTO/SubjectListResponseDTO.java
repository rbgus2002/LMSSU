package com.project.LMSSU.DTO;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class SubjectListResponseDTO {
    private List weeksSubjectListDTO;
    private List subjectNoticeListDTO;

    @Builder
    public SubjectListResponseDTO(List weeksSubjectListDTO, List subjectNoticeListDTO) {
        this.weeksSubjectListDTO = weeksSubjectListDTO;
        this.subjectNoticeListDTO = subjectNoticeListDTO;
    }
}
