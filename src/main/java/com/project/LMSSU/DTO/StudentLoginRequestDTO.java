package com.project.LMSSU.DTO;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
public class StudentLoginRequestDTO {
    private Long studentId;
    private Integer userId;
    private String pwd;

    public StudentLoginRequestDTO() {
    }

    @Builder
    public StudentLoginRequestDTO(Long studentId, Integer userId, String pwd) {
        this.studentId = studentId;
        this.userId = userId;
        this.pwd = pwd;
    }

    @Override
    public String toString() {
        return "StudentLoginRequestDTO{" +
                "studentId=" + studentId +
                ", userId=" + userId +
                ", pwd='" + pwd + '\'' +
                '}';
    }
}
