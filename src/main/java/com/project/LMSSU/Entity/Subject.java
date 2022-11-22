package com.project.LMSSU.Entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.C;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class Subject {
    @Id @Column(name = "subject_id")
    private Long id;

    private String subjectName;

    private String professorName;
    private String homepageAddress;

    private LocalDateTime updateTime;

    @Builder
    public Subject(Long id, String subjectName, String professorName, String homepageAddress, LocalDateTime updateTime) {
        this.id = id;
        this.subjectName = subjectName;
        this.professorName = professorName;
        this.homepageAddress = homepageAddress;
        this.updateTime = updateTime;
    }
}
