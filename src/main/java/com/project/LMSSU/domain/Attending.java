package com.project.LMSSU.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class Attending {
    @Id @GeneratedValue
    private Long attending_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    private Subject subject_id;

    @Builder
    public Attending(Student student_id, Subject subject_id) {
        this.student_id = student_id;
        this.subject_id = subject_id;
    }
}
