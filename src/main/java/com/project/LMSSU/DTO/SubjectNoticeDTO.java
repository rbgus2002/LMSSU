package com.project.LMSSU.DTO;

import lombok.Builder;
import lombok.Data;

@Data
public class SubjectNoticeDTO {
    private String title;
    private String noticeLink;

    @Builder
    public SubjectNoticeDTO(String title, String noticeLink) {
        this.title = title;
        this.noticeLink = noticeLink;
    }
}
