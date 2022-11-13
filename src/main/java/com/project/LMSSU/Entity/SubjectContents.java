package com.project.LMSSU.Entity;

import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
public class SubjectContents {
    @Id @GeneratedValue
    private Long subject_contents_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    private Subject subject;

    private String contentType;
    private String title;
    private int week;
    private LocalDate endDate;
    private int sequence;

    @Builder
    public SubjectContents(Subject subject, String contentType, String title, int week, LocalDate endDate, int sequence) {
        this.subject = subject;
        this.contentType = contentType;
        this.title = title;
        this.week = week;
        this.endDate = endDate;
        this.sequence = sequence;
    }
}
