package com.project.LMSSU.Entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor
public class ToDo {
    @Id @GeneratedValue
    private Long todoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    private Subject subject;

    private int week;
    private String content;
    private Boolean isDone;
    private Boolean isUsed;

    @Builder
    public ToDo(Student student, Subject subject, int week, String content){
        this.student = student;
        this.subject = subject;
        this.week = week;
        this.content = content;
        this.isDone = false;
        this.isUsed = true;
    }

}
