package com.project.LMSSU.Entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
public class MajorNotice {
    @Id @GeneratedValue
    private Long majorNoticeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "major_name")
    private Major major;

    private String title;
    private String pageAddress;

    @Builder
    public MajorNotice(Major major, String title, String pageAddress){
        this.major = major;
        this.title = title;
        this.pageAddress = pageAddress;
    }
}
