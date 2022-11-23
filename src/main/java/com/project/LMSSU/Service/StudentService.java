package com.project.LMSSU.Service;

import com.project.LMSSU.DTO.StudentLoginRequestDTO;
import com.project.LMSSU.DTO.StudentMajorAndNameRequestDTO;
import com.project.LMSSU.Entity.Attending;
import com.project.LMSSU.Entity.Major;
import com.project.LMSSU.Entity.Student;
import com.project.LMSSU.Entity.Subject;
import com.project.LMSSU.Repository.AttendingRepository;
import com.project.LMSSU.Repository.MajorRepository;
import com.project.LMSSU.Repository.StudentRepository;
import com.project.LMSSU.Repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RequiredArgsConstructor
@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final MajorRepository majorRepository;
    private final AttendingRepository attendingRepository;
    private final SubjectRepository subjectRepository;


    /*
    로그인
     */
    public Map signIn(StudentLoginRequestDTO dto) throws InterruptedException {
        Map map = new HashMap();
        map.put("studentId", dto.getStudentId());
        map.put("user", "original");

        Optional<Student> studentOptional = studentRepository.findById(dto.getStudentId());

        // 회원 여부 체크
        if(studentOptional.isEmpty()){
            // student 등록
            studentRepository.save(Student.builder()
                            .name(null)
                            .major(null)
                            .id(dto.getStudentId())
                    .build());

            // 수강 과목 정보 저장
            saveAttending(dto);

            // 새로 등록한 회원
            map.put("user", "new");

        }else{
            // 학과 입력이 안되어 있는 경우
            if(studentOptional.get().getMajor() == null){
                map.put("user", "new");
            }
        }

        return map;
    }

    /*
    특정 학생의 수강중인 과목들을 Attending Table에 저장한다.
    가져온 과목이 Subject Table에 등록되지 않았으면 해준다.
    */
    @Transactional
    public void saveAttending(StudentLoginRequestDTO dto) throws InterruptedException {
        // Student 예외처리
        Optional<Student> student = studentRepository.findById(dto.getStudentId());
        if (student.isEmpty()) {
            System.out.println("studentId Error"); // 로그로 찍기
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "studentId error");
        }

        // student가 수강 중인 과목의 subjectId List 가져오기
        List<Long> attendingSubjectId = new ArrayList<>();
        List<Attending> attendingList = attendingRepository.findAttendingByStudentId(student.get().getId());
        for (Attending attending : attendingList) {
            attendingSubjectId.add(attending.getSubject().getId());
        }

        // LMSCrawling 객체 생성 (init, login)
        LMSCrawling lmsCrawling = new LMSCrawling(dto);

        // 크롤링해서 수강중인 subjectId 가져오기
        List<Long> subjectIdList = lmsCrawling.getSubjectId();

        for (Long subjectId : subjectIdList) {
            Optional<Subject> subjectOptional = subjectRepository.findById(subjectId);

            // subject 없으면 추가
            if (subjectOptional.isEmpty()) {
                Map<Object, String> subjectInfo = lmsCrawling.getSubjectInfo(subjectId.toString());
                if (subjectInfo != null) {
                    subjectRepository.save(Subject.builder()
                            .homepageAddress(subjectInfo.get("homepageAddress"))
                            .professorName(subjectInfo.get("professorName"))
                            .subjectName(subjectInfo.get("subjectName"))
                            .id(subjectId)
                            .updateTime(null)
                            .build());
                }
            }

            // attending 없으면 추가
            if (!attendingSubjectId.contains(subjectId)) {
                Subject subject = subjectRepository.findById(subjectId).get();
                attendingRepository.save(Attending.builder()
                        .student(student.get())
                        .subject(subject)
                        .build());
            }
        }

        lmsCrawling.quitCrawling();
    }

    /*
    Student에 등록되어 있는 학생에게 학과와 이름을 입력 받아 저장한다.
     */
    public Map updateMajorAndName(StudentMajorAndNameRequestDTO dto){
        // Student 예외처리
        Optional<Student> studentOptional = studentRepository.findById(dto.getStudentId());
        if (studentOptional.isEmpty()) {
            System.out.println("studentId Error"); // 로그로 찍기
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "studentId error");
        }
        Student student = studentOptional.get();

        // Major 예외처리
        Optional<Major> majorOptional = majorRepository.findByMajorName(dto.getMajor());
        if (majorOptional.isEmpty()) {
            System.out.println("majorName Error"); // 로그로 찍기
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "majorName Error");
        }

        // major, name 저장
        student.setMajor(majorOptional.get());
        student.setName(dto.getStudentName());
        studentRepository.save(student);

        Map map = new HashMap();
        map.put("studentId", dto.getStudentId());
        return map;
    }

}
