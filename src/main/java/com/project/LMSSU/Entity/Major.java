package com.project.LMSSU.Entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Major {
    @Id
    @GeneratedValue
    private String majorName;

    private String homepageAddress;

    @Builder
    public Major(String majorName, String homepageAddress) {
        this.majorName = majorName;
        this.homepageAddress = homepageAddress;
    }
}
