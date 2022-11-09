package com.project.LMSSU.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Major {
    @Id
    private String major_name;

    private String homepage_address;

    @Builder
    public Major(String major_name, String homepage_address){
        this.major_name = major_name;
        this.homepage_address = homepage_address;
    }
}
