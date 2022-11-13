package com.project.LMSSU.Entity;

import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
public class IncompleteNoticeContents {
    @Id @GeneratedValue
    private Long incompleteNoticeContentsId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_notice_id")
    private SubjectNotice subjectNotice;

    @Builder
    public IncompleteNoticeContents(Student student, SubjectNotice subjectNotice) {
        this.student = student;
        this.subjectNotice = subjectNotice;
    }
}
