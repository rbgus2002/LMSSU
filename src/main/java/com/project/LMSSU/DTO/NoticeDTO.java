package com.project.LMSSU.DTO;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
public class NoticeDTO implements Comparable<NoticeDTO> {
    private LocalDate date;
    private String title;
    private String url;

    @Builder
    public NoticeDTO(LocalDate date, String title, String url) {
        this.date = date;
        this.title = title;
        this.url = url;
    }

    @Override
    public int compareTo(NoticeDTO noticeDTO) { // 최신 순으로 정렬하기 위한 메소드
        return noticeDTO.date.compareTo(date);
    }
}
