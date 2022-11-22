package com.project.LMSSU.Entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class SubjectContents {
    @Id @GeneratedValue
    private Long subjectContentsId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    private Subject subject;

    private String contentType;
    private String title;
    private int week;
    private LocalDate endDate;

    @Builder
    public SubjectContents(Subject subject, String contentType, String title, int week, LocalDate endDate) {
        this.subject = subject;
        this.contentType = contentType;
        this.title = title;
        this.week = week;
        this.endDate = endDate;
    }
}
