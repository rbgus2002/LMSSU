package com.project.LMSSU.DTO;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class NoticeResponseDTO {
    private int page;
    private List ssuNoticeDTO;

    @Builder
    public NoticeResponseDTO(int page, List ssuNoticeDTO) {
        this.page = page;
        this.ssuNoticeDTO = ssuNoticeDTO;
    }
}
