package com.project.LMSSU.Service;



import com.project.LMSSU.DTO.IncompleteContentsResponseDTO;
import com.project.LMSSU.DTO.SubjectContents_endDateDTO;
import com.project.LMSSU.Entity.Student;
import com.project.LMSSU.Entity.Subject;
import com.project.LMSSU.Entity.SubjectContents;
import com.project.LMSSU.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class IncompleteContentsService {
    private final AttendingRepository attendingRepository;
    private final SubjectRepository subjectRepository;
    private final IncompleteSubjectContentsRepository incompleteSubjectContentsRepository;
    private final SubjectContentsRepository subjectContentsRepository;
    private final StudentRepository studentRepository;


    /*
    마저 끝내기 ㄱ
     */
    public List<IncompleteContentsResponseDTO> getIncompleteContents(Long studentId){
        List<IncompleteContentsResponseDTO> answer = new ArrayList<>();

        // Student 예외처리
        Optional<Student> student = studentRepository.findById(studentId);
        if (student.isEmpty()) {
            System.out.println("studentId Error"); // 로그로 찍기
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "studentId error");
        }

        // 수강중인 과목List 가져오기
        List<Subject> subjectList = attendingRepository.findSubjectByStudentId(studentId);
        for(Subject subject : subjectList){
            // 강의 컨텐츠 가져오기
            List<SubjectContents_endDateDTO> subjectContentsEndDateDTOList = new ArrayList<>();
            List<SubjectContents> subjectContentsList = subjectContentsRepository.findBySubjectId(subject.getId());
            for(SubjectContents subjectContents : subjectContentsList){
                SubjectContents_endDateDTO subjectContentsEndDateDTO = SubjectContents_endDateDTO.builder()
                        .title(subjectContents.getTitle())
                        .contentsType(subjectContents.getContentType())
                        .endDate(subjectContents.getEndDate())
                        .build();

                subjectContentsEndDateDTOList.add(subjectContentsEndDateDTO);
            }

            // IncompleteContentsResponseDTO 객체 초기화
            IncompleteContentsResponseDTO dto = IncompleteContentsResponseDTO.builder()
                    .subjectName(subject.getSubjectName())
                    .professorName(subject.getProfessorName())
                    .link(subject.getHomepageAddress())
                    .subjectContents_endDateDTOList(subjectContentsEndDateDTOList)
                    .build();

            answer.add(dto);
        }

        return answer;
    }
}
