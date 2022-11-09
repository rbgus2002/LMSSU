package com.project.LMSSU.domain;

import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
public class SubjectContents {
    @Id @GeneratedValue
    private Long subject_contents_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    private Subject subject;

    private String contents_type;
    private String title;
    private String page_address;
    private int week;
    private LocalDateTime deadline;
    private int sequence;

    @Builder
    public SubjectContents(Subject subject, String contents_type, String title, String page_address, int week, LocalDateTime deadline, int sequence) {
        this.subject = subject;
        this.contents_type = contents_type;
        this.title = title;
        this.page_address = page_address;
        this.week = week;
        this.deadline = deadline;
        this.sequence = sequence;
    }
}
