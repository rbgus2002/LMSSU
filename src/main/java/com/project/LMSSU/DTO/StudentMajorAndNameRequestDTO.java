package com.project.LMSSU.DTO;

import lombok.Builder;
import lombok.Data;

@Data
public class StudentMajorAndNameRequestDTO {
    private Long studentId;
    private String major;
    private String studentName;

    @Builder
    public StudentMajorAndNameRequestDTO(Long studentId, String major, String studentName) {
        this.studentId = studentId;
        this.major = major;
        this.studentName = studentName;
    }

    public StudentMajorAndNameRequestDTO() {
    }
}
