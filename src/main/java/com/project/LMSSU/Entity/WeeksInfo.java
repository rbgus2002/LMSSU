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

    private LocalDate start_date;
    private LocalDate end_date;

    @Builder
    public WeeksInfo(Integer week, LocalDate start_date, LocalDate end_date){
        this.week = week;
        this.start_date = start_date;
        this.end_date = end_date;
    }

}
