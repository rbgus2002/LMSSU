package com.project.LMSSU.Service;


import com.project.LMSSU.Repository.AttendingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.management.Query;

@RequiredArgsConstructor
@Service
public class TestService {
    private final AttendingRepository attendingRepository;

    public void test(){
        System.out.println(attendingRepository.findSubjectByStudentId(20182662L));
    }
}
