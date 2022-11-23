package com.project.LMSSU.DTO;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
public class StudentLoginRequestDTO {
    private Long studentId;
    private Integer userId;
    private String pwd;

    @Builder
    public StudentLoginRequestDTO(Long studentId, Integer userId, String pwd) {
        this.studentId = studentId;
        this.userId = userId;
        this.pwd = pwd;
    }
}
