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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "major_id")
    private Major major;

    @Builder
    public Student(Long id, String name, Major major) {
        this.id = id;
        this.name = name;
        this.major = major;
    }
}
