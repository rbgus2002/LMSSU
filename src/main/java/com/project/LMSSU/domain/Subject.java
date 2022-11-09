package com.project.LMSSU.domain;

import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
public class Subject {
    @Id
    private Long subject_id;

    private String subject_name;
    private String professor_name;
    private String homepage_address;

    @Builder
    public Subject(Long subject_id, String subject_name, String professor_name, String homepage_address){
        this.subject_id = subject_id;
        this.subject_name = subject_name;
        this.professor_name = professor_name;
        this.homepage_address = homepage_address;
    }
}
