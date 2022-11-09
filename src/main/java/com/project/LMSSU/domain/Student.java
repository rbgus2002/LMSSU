package com.project.LMSSU.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Student {
    @Id
    private Long student_id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "major_name")
    private Major major;

    @Builder
    public Student(Long student_id, String name, Major major){
        this.student_id = student_id;
        this.name = name;
        this.major = major;
    }
}
