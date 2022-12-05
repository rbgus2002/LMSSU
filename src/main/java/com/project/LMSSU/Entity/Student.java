package com.project.LMSSU.Entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class Student {
    @Id @Column(name = "student_id")
    private Long id;
    private String name;
    private String majorName;

    @Builder
    public Student(Long id, String name, String majorName) {
        this.id = id;
        this.name = name;
        this.majorName = majorName;
    }
}
