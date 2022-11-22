package com.project.LMSSU.DTO;

import com.project.LMSSU.Entity.Subject;
import com.project.LMSSU.Entity.SubjectContents;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CalendarDTO {
    private String title;
    private String contentType;
    private String pageURL;
    private LocalDate endDate;

    @Builder
    public CalendarDTO(Subject subject, SubjectContents subjectContents) {
        this.title = subject.getSubjectName();
        this.contentType = subjectContents.getContentType();
        this.pageURL = subject.getHomepageAddress();
        this.endDate = subjectContents.getEndDate();
    }
}
