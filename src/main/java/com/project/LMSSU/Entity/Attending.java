package com.project.LMSSU.Entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class Attending {
    @Id @GeneratedValue
    private Long attendingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student studentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    private Subject subjectId;

    @Builder
    public Attending(Student studentId, Subject subjectId) {
        this.studentId = studentId;
        this.subjectId = subjectId;
    }
}
