package com.project.LMSSU.DTO;

import com.project.LMSSU.Entity.ExamSchedule;
import com.project.LMSSU.Entity.Student;
import com.project.LMSSU.Entity.Subject;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ExamScheduleRequestDTO {
    private LocalDate date;
    private Long studentId;
    private Long subjectId;

    @Builder
    public ExamScheduleRequestDTO(LocalDate date, Long studentId, Long subjectId) {
        this.date = date;
        this.studentId = studentId;
        this.subjectId = subjectId;
    }
}
