package com.project.LMSSU.Service;


import com.project.LMSSU.Entity.WeeksInfo;
import com.project.LMSSU.Repository.AttendingRepository;
import com.project.LMSSU.Repository.WeeksInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.management.Query;
import java.time.LocalDate;

@RequiredArgsConstructor
@Service
public class TestService {
    private final AttendingRepository attendingRepository;
    private final WeeksInfoRepository weeksInfoRepository;
    public void test(){
        System.out.println(attendingRepository.findSubjectByStudentId(20182662L));
    }

    public void addWeeks() {
        LocalDate start = LocalDate.of(2022,9,1);
        LocalDate end = LocalDate.of(2022,9,7);
        for(int i=1; i<=15; i++) {
            WeeksInfo weeksInfo = WeeksInfo.builder()
                    .week(i)
                    .startDate(start.plusDays((i-1)*7))
                    .endDate(end.plusDays((i-1)*7))
                    .build();
            weeksInfoRepository.save(weeksInfo);
        }
    }

}
