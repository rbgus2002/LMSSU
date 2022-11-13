package com.project.LMSSU.Service;

import com.project.LMSSU.DTO.LMSCrawlingRequestDTO;
import com.project.LMSSU.Entity.Attending;
import com.project.LMSSU.Entity.Student;
import com.project.LMSSU.Entity.Subject;
import com.project.LMSSU.Entity.SubjectContents;
import com.project.LMSSU.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

@RequiredArgsConstructor
@Service
public class LMSCrawlingService {

    private List<CrawlingSubject> crawlingSubjectList;

    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private final MajorRepository majorRepository;
    private final AttendingRepository attendingRepository;
    private final SubjectContentsRepository subjectContentsRepository;

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
            System.out.println("과목 이름 : " + subject.getName());

            // subject 저장
            System.out.println("Subject Table 저장");
            Optional<Subject> optionalSubject = subjectRepository.findBySubjectName(subject.getName());
            Subject newSubject;
            if (optionalSubject.isEmpty()) {
                newSubject = subjectRepository.save(Subject.builder()
                        .subjectName(subject.getName())
                        .professorName(subject.getProfessor())
                        .homepageAddress(subject.getHomepageLink())
                        .build());

            }else{
                newSubject = optionalSubject.get();
            }

            // attending Table 저장
            System.out.println("Attending Table 저장");
            attendingRepository.save(Attending.builder()
                            .studentId(student.get())
                            .subjectId(newSubject)
                    .build());


            // subject_contents Table 저장
            // subject_id에 해당하는 tuple 삭제 후 저장 (FK Attribute cascade 삭제)
            for(ContentPerWeek contentPerWeek : subject.getContentPerWeekList()){
                for(Content content : contentPerWeek.getContentList()){
                    //LocalDate 객체 생성
                    LocalDate localDate = null;
                    StringTokenizer st;
                    String contentEndDate = content.getEndDate();
                    if(!contentEndDate.equals("None")){
                        st = new StringTokenizer(contentEndDate, " ");
                        Integer year = LocalDate.now().getYear();
                        String month = st.nextToken().replaceAll("[^0-9]", ""); // 숫자만 추출
                        System.out.println("month : " + month);
                        String day = st.nextToken().replaceAll("[^0-9]", "");
                        localDate = LocalDate.of(year, Integer.parseInt(month), Integer.parseInt(day));

                        System.out.println("localdate : " + localDate.toString());
                    }

                    subjectContentsRepository.save(SubjectContents.builder()
                                    .contentType(content.getType())
                                    .endDate(localDate)
                                    .sequence(1)
                                    .title(content.getName())
                                    .week(contentPerWeek.getWeeks())
                                    .subject(newSubject)
                            .build());
                }
            }







        }





        // Map 리턴
        Map<String, Object> map = new HashMap<>();
        map.put("studentId", lmsCrawlingRequestDTO.getStudentId());
        return map;
    }


}
