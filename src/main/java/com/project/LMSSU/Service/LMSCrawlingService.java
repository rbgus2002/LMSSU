package com.project.LMSSU.Service;

import com.project.LMSSU.DTO.LMSCrawlingRequestDTO;
import com.project.LMSSU.Entity.Student;
import com.project.LMSSU.Entity.Subject;
import com.project.LMSSU.Repository.MajorRepository;
import com.project.LMSSU.Repository.StudentRepository;
import com.project.LMSSU.Repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class LMSCrawlingService {

    private List<CrawlingSubject> crawlingSubjectList;

    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private final MajorRepository majorRepository;

    /*
    크롤링 정보 DB 저장
     */
    public Map saveLMSInformation(LMSCrawlingRequestDTO lmsCrawlingRequestDTO) throws IOException, InterruptedException {
        // Student 예외처리
        Optional<Student> student = studentRepository.findById(lmsCrawlingRequestDTO.getStudentId());
        if (student.isEmpty()) {
            System.out.println("studentId Error"); // 로그로 찍기
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "studentId error");
        }

        // LMS 크롤링
        crawlingSubjectList = LMSCrawling.run(lmsCrawlingRequestDTO.getUserId(), lmsCrawlingRequestDTO.getPwd());


        for (CrawlingSubject subject : crawlingSubjectList) {
            // Subject 저장
            Optional<Subject> optionalSubject = subjectRepository.findBySubjectName(subject.getName());
            if (optionalSubject.isEmpty()) {
                subjectRepository.save(Subject.builder()
                        .subjectName(subject.getName())
                        .professorName(subject.getProfessor())
                        .homepageAddress(subject.getHomepageLink())
                        .build());
            }

            //
        }





        // Map 리턴
        Map<String, Object> map = new HashMap<>();
        map.put("studentId", lmsCrawlingRequestDTO.getStudentId());
        return map;
    }


}
