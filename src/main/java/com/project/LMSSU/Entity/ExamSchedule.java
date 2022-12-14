package com.project.LMSSU.Entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
public class ExamSchedule {
    @Id @GeneratedValue
    private Long examScheduleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    private Subject subject;

    private LocalDate date;
    @Column(name = "is_used")
    private Boolean isUsed;

    @Builder
    public ExamSchedule(Student student, Subject subject, LocalDate date) {
        this.student = student;
        this.subject = subject;
        this.date = date;
        this.isUsed = true;
    }
}
