package com.project.LMSSU.DTO;

import com.project.LMSSU.Entity.ExamSchedule;
import com.project.LMSSU.Entity.Subject;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ExamScheduleDTO {
    private LocalDate date;
    private String title;

    @Builder
    public ExamScheduleDTO(ExamSchedule examSchedule, Subject subject) {
        this.date = examSchedule.getDate();
        this.title = subject.getSubjectName();
    }
}
