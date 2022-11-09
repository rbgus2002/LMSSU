package com.project.LMSSU.domain;

import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
public class ExamSchedule {
    @Id @GeneratedValue
    private int exam_schedule_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    private Subject subject;

    private LocalDate date;
    private Boolean is_used;

    @Builder
    public ExamSchedule(Student student, Subject subject, LocalDate date) {
        this.student = student;
        this.subject = subject;
        this.date = date;
        this.is_used = true;
    }
}
