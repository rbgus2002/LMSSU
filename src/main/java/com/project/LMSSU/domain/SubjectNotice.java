package com.project.LMSSU.domain;

import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
public class SubjectNotice {
    @Id @GeneratedValue
    private int subject_notice_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    private Subject subject;

    private String title;
    private String contents;
    private String page_address;

    @Builder
    public SubjectNotice(Subject subject, String title, String contents, String page_address) {
        this.subject = subject;
        this.title = title;
        this.contents = contents;
        this.page_address = page_address;
    }
}
