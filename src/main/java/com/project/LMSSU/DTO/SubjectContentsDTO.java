package com.project.LMSSU.DTO;

import lombok.Builder;
import lombok.Data;

@Data
public class SubjectContentsDTO {
    private String title;
    private String contentsType;

    @Builder
    public SubjectContentsDTO(String title, String contentsType) {
        this.title = title;
        this.contentsType = contentsType;
    }
}
