package com.project.LMSSU.Entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class Subject {
    @Id
    private Long subjectId;

    private String subjectName;

    private String professorName;
    private String homepageAddress;

    private LocalDateTime updateTime;

    @Builder
    public Subject(Long subjectId, String subjectName, String professorName, String homepageAddress, LocalDateTime updateTime) {
        this.subjectId = subjectId;
        this.subjectName = subjectName;
        this.professorName = professorName;
        this.homepageAddress = homepageAddress;
        this.updateTime = updateTime;
    }
}
