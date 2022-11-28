package com.project.LMSSU.DTO;

import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class SubjectListResponseDTO {
    private WeeksSubjectListDTO weeksSubjectListDTO;
    private List<SubjectNoticeListDTO> subjectNoticeListDTO;

    @Builder
    public SubjectListResponseDTO(WeeksSubjectListDTO weeksSubjectListDTO, List subjectNoticeListDTO) {
        this.weeksSubjectListDTO = weeksSubjectListDTO;
        this.subjectNoticeListDTO = subjectNoticeListDTO;
    }
}
