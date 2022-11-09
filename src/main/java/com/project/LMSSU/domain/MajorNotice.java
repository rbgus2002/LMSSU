package com.project.LMSSU.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
public class MajorNotice {
    @Id @GeneratedValue
    private Long major_notice_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "major_name")
    private Major major;

    private String title;
    private String page_address;

    @Builder
    public MajorNotice(Major major, String title, String page_address){
        this.major = major;
        this.title = title;
        this.page_address = page_address;
    }
}
