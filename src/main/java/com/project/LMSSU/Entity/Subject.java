package com.project.LMSSU.Entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class Subject {
    @Id
    @GeneratedValue
    private Long subjectId;


    private String subjectName;

    private String professorName;
    private String homepageAddress;

    @Builder
    public Subject(String subjectName, String professorName, String homepageAddress) {
        this.subjectName = subjectName;
        this.professorName = professorName;
        this.homepageAddress = homepageAddress;
    }
}
