package com.project.LMSSU.DTO;

import com.project.LMSSU.Entity.SubjectContents;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
public class IncompleteTaskDTO {
    private String contentType;
    private String title;
    private LocalDate endDate;
    //private String pageAddress;


    @Builder
    public IncompleteTaskDTO(SubjectContents subjectContents) {
        this.contentType = subjectContents.getContentType();
        this.title = subjectContents.getTitle();
        this.endDate = subjectContents.getEndDate();
        //this.pageAddress = subjectContents.getPageAddress();

    }
}
