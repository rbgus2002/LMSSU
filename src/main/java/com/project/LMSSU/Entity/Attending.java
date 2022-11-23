package com.project.LMSSU.Entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@NoArgsConstructor
@Getter
public class Attending {
    @Id @GeneratedValue
    private Long attendingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    @NotNull
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    @NotNull
    private Subject subject;

    @Builder
    public Attending(Student student, Subject subject) {
        this.student = student;
        this.subject = subject;
    }
}
