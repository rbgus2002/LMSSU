package com.project.LMSSU.Entity;

import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
public class IncompleteSubjectContents {
    @Id @GeneratedValue
    private Long incomplete_subject_contents_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_contents_id")
    private SubjectContents subject_contents;

    @Builder
    public IncompleteSubjectContents(Student student, SubjectContents subject_contents) {
        this.student = student;
        this.subject_contents = subject_contents;
    }
}
