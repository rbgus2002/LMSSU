package com.project.LMSSU.Entity;

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
    @GeneratedValue
    private Long studentId;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "major_id")
    private Major major;

    @Builder
    public Student(Long studentId, String name, Major major){
        this.studentId = studentId;
        this.name = name;
        this.major = major;
    }
}
