package com.project.LMSSU.domain;

import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
public class ToDo {
    @Id @GeneratedValue
    private int todo_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    private Subject subject;

    private int week;
    private String content;
    private Boolean is_done;
    private Boolean is_used;

    @Builder
    public ToDo(Student student, Subject subject, int week, String content){
        this.student = student;
        this.subject = subject;
        this.week = week;
        this.content = content;
        this.is_done = false;
        this.is_used = true;
    }

}
