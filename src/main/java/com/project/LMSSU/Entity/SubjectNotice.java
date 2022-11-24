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
public class SubjectNotice {
    @Id
    private Long subjectNoticeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    private Subject subject;

    private String title;
    private String noticeLink;
    private LocalDate localDate;

    @Builder
    public SubjectNotice(Long subjectNoticeId, Subject subject, String title, String noticeLink, LocalDate localDate) {
        this.subjectNoticeId = subjectNoticeId;
        this.subject = subject;
        this.title = title;
        this.noticeLink = noticeLink;
        this.localDate = localDate;
    }
}
