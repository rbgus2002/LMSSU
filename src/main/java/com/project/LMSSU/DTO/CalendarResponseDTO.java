package com.project.LMSSU.DTO;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class CalendarResponseDTO {
    private List calendarDTO;
    private List examScheduleDTO;

    @Builder
    public CalendarResponseDTO(List calendarDTO, List examScheduleDTO) {
        this.calendarDTO = calendarDTO;
        this.examScheduleDTO = examScheduleDTO;
    }
}
