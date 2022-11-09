package com.project.LMSSU.domain;

import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
public class IncompleteNoticeContents {
    @Id @GeneratedValue
    private int incomplete_notice_contents_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_notice_id")
    private SubjectNotice subject_notice;

    @Builder
    public IncompleteNoticeContents(Student student, SubjectNotice subject_notice) {
        this.student = student;
        this.subject_notice = subject_notice;
    }
}
