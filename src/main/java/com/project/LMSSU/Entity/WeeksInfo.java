package com.project.LMSSU.Entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@Data
public class WeeksInfo {
    @Id
    private Integer week;

    private LocalDate startDate;
    private LocalDate endDate;

    @Builder
    public WeeksInfo(Integer week, LocalDate startDate, LocalDate endDate){
        this.week = week;
        this.startDate = startDate;
        this.endDate = endDate;
    }

}
