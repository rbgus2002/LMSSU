package com.project.LMSSU.DTO;

import lombok.Builder;
import lombok.Data;

@Data
public class NoticeDTO {
    private String date;
    private String title;
    private String url;

    @Builder
    public NoticeDTO(String date, String title, String url) {
        this.date = date;
        this.title = title;
        this.url = url;
    }
}
