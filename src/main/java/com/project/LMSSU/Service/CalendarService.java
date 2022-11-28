package com.project.LMSSU.Service;

import com.project.LMSSU.DTO.CalendarDTO;
import com.project.LMSSU.DTO.CalendarResponseDTO;
import com.project.LMSSU.DTO.ExamScheduleDTO;
import com.project.LMSSU.DTO.ExamScheduleRequestDTO;
import com.project.LMSSU.Entity.*;
import com.project.LMSSU.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CalendarService {
    private final ExamScheduleRepository examScheduleRepository;
    private final SubjectRepository subjectRepository;
    private final SubjectContentsRepository subjectContentsRepository;
    private final AttendingRepository attendingRepository;
    private final StudentRepository studentRepository;

    /*
    DB에 저장되어 있는 캘린더 정보를 보내준다.
    */
    public CalendarResponseDTO getCalendarData(Long studentId) {
        List<ExamSchedule> examSchedules = examScheduleRepository.findByStudentId(studentId);
        List<ExamScheduleDTO> examScheduleDTOS = new ArrayList<>();
        List<Attending> attendings = attendingRepository.findAttendingByStudentId(studentId);
        List<CalendarDTO> calendarDTOS = new ArrayList<>();

        // examScheduleDTO List
        for (ExamSchedule examSchedule : examSchedules) {
            if(examSchedule.getIsUsed()) // is_used == true
            {
                Optional<Subject> subject = subjectRepository.findById(examSchedule.getSubject().getId());
                if (subject.isEmpty()) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "subject error");
                }
                ExamScheduleDTO examScheduleDTO = ExamScheduleDTO.builder()
                        .examSchedule(examSchedule)
                        .subject(subject.get())
                        .build();
                examScheduleDTOS.add(examScheduleDTO);
            }
        }

        // calendarDTO List
        for (Attending attending : attendings) {
            List<SubjectContents> subjectContents = subjectContentsRepository.findBySubjectId(attending.getSubject().getId());
            for (SubjectContents subjectContent : subjectContents) {
                // 마감일 NULL인 경우 처리
                if(subjectContent.getEndDate()==null)
                    continue;
                else {
                    CalendarDTO calendarDTO = CalendarDTO.builder()
                            .subject(attending.getSubject())
                            .subjectContents(subjectContent)
                            .build();
                    calendarDTOS.add(calendarDTO);
                }
            }
        }

        CalendarResponseDTO calendarResponseDTO = CalendarResponseDTO.builder()
                .calendarDTO(calendarDTOS)
                .examScheduleDTO(examScheduleDTOS)
                .build();

        return calendarResponseDTO;
    }

    /*
    시험 일정을 추가한다.
    */
    public Map addExamSchedule(ExamScheduleRequestDTO dto){
        Map<String, Object> map = new HashMap<>();
        Optional<Attending> attending = attendingRepository.findAttendingByStudentIdAndSubjectId(dto.getStudentId(), dto.getSubjectId());
        // 수강하지 않는 과목인 경우
        if(attending.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Data");
        // 올바른 정보가 요청된 경우
        else {
            Optional<ExamSchedule> examScheduleOptional = examScheduleRepository.findByStudentIdAndSubjectIdAndDate(dto.getStudentId(), dto.getSubjectId(), dto.getDate());
            // addExamSchedule
            if(examScheduleOptional.isEmpty()) {
                Optional<Subject> subjectOptional = subjectRepository.findById(dto.getSubjectId());
                Optional<Student> studentOptional = studentRepository.findById(dto.getStudentId());
                Subject subject = subjectOptional.get();
                Student student = studentOptional.get();
                ExamSchedule examSchedule = ExamSchedule.builder()
                        .student(student)
                        .subject(subject)
                        .date(dto.getDate())
                        .build();
                examScheduleRepository.save(examSchedule);
                map.put("studentId", examSchedule.getStudent().getId());
            }
            // 이미 데이터가 존재하는 경우
            else {
                ExamSchedule examSchedule = examScheduleOptional.get();
                if(examSchedule.getIsUsed()) // is_used == true
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Overlap");
                else {
                    examSchedule.setIsUsed(true);
                    examScheduleRepository.save(examSchedule);
                    map.put("studentId", examSchedule.getStudent().getId());
                }

            }
        }
        return map;
    }

    /*
    시험 일정을 삭제한다.
    */
    public Map deleteExamSchedule(ExamScheduleRequestDTO dto) {
        Map<String, Object> map = new HashMap<>();
        Optional<Attending> attending = attendingRepository.findAttendingByStudentIdAndSubjectId(dto.getStudentId(), dto.getSubjectId());
        // 수강하지 않는 과목인 경우
        if (attending.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Data");
            // 올바른 정보가 요청된 경우
        else {
            Optional<ExamSchedule> examScheduleOptional = examScheduleRepository.findByStudentIdAndSubjectIdAndDate(dto.getStudentId(), dto.getSubjectId(), dto.getDate());
            // 삭제하고자 하는 데이터가 없는 경우
            if (examScheduleOptional.isEmpty())
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No Data");
            // deleteExamSchedule
            else {
                ExamSchedule examSchedule = examScheduleOptional.get();
                if(examSchedule.getIsUsed()) { // is_used == true
                    examSchedule.setIsUsed(false);
                    examScheduleRepository.save(examSchedule);
                    map.put("studentId", examSchedule.getStudent().getId());
                }
                else // is_used == false
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No Data");
            }
        }
        return map;
    }
}