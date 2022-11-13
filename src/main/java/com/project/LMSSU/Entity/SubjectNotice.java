package com.project.LMSSU.Entity;

import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
public class SubjectNotice {
    @Id @GeneratedValue
    private Long subjectNoticeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    private Subject subject;

    private String title;
    private String contents;
    private String pageAddress;

    @Builder
    public SubjectNotice(Subject subject, String title, String contents, String pageAddress) {
        this.subject = subject;
        this.title = title;
        this.contents = contents;
        this.pageAddress = pageAddress;
    }
}
