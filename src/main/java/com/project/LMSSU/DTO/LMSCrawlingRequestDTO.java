package com.project.LMSSU.DTO;

import lombok.Getter;

@Getter
public class LMSCrawlingRequestDTO {
    private Long studentId;
    private Integer userId;
    private String pwd;


    // 생성자 임시로 만들어줌

    public LMSCrawlingRequestDTO(Long studentId, Integer userId, String pwd) {
        this.studentId = studentId;
        this.userId = userId;
        this.pwd = pwd;
    }
}
