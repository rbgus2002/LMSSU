package com.project.LMSSU.DTO;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class SubjectNoticeListDTO {
    private String title;
    private String homepageAddress;
    private List<SubjectNoticeDTO> subjectNoticeDTO; // 최신 순으로 정렬

    @Builder
    public SubjectNoticeListDTO(String title, String homepageAddress, List subjectNoticeDTO) {
        this.title = title;
        this.homepageAddress = homepageAddress;
        this.subjectNoticeDTO = subjectNoticeDTO;
    }
}
