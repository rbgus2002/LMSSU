package com.project.LMSSU.Service;

import com.project.LMSSU.DTO.*;
import com.project.LMSSU.Entity.*;
import com.project.LMSSU.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
@RequiredArgsConstructor
public class SubjectListService {
    private final SubjectRepository subjectRepository;
    private final SubjectContentsRepository subjectContentsRepository;
    private final AttendingRepository attendingRepository;
    private final StudentRepository studentRepository;
    private final ToDoRepository toDoRepository;
    private final SubjectNoticeRepository subjectNoticeRepository;
    private final WeeksInfoRepository weeksInfoRepository;

    public SubjectListResponseDTO getSubjectListData(Long studentId) {
        List<Attending> attendings = attendingRepository.findAttendingByStudentId(studentId);
        List<Subject> subjects = new ArrayList<>();
        List<List<SubjectContents>> subjectContents = new ArrayList<>();
        List<List<SubjectNotice>> subjectNotices = new ArrayList<>();
        List<List<ToDo>> toDos = new ArrayList<>();
        List<WeeksSubjectListDTO> weeksSubjectListDTOS = new ArrayList<>();
        List<WeeksInfo> weeksInfos = weeksInfoRepository.findAll();
        List<SubjectNoticeListDTO> subjectNoticeListDTOS = new ArrayList<>();
        // 학번, 과목코드로 필요한 정보 가져옴
        for (Attending attending : attendings) {
            Optional<Subject> subjectOptional = subjectRepository.findById(attending.getSubject().getId());
            if (subjectOptional.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "subject error");
            }
            Subject subject = subjectOptional.get();
            subjects.add(subject);
            subjectContents.add(subjectContentsRepository.findBySubjectIdOrderByWeekAsc(subject.getId()));
            subjectNotices.add(subjectNoticeRepository.findBySubjectIdOrderByLocalDateDesc(subject.getId()));
            toDos.add(toDoRepository.selectJPQLByStudentIdAndSubjectId(studentId, subject.getId()));
        }

        // 주차별로 weeksSubjectListDTO List 생성
        for (WeeksInfo weeksInfo : weeksInfos) {
            List<SubjectDTO> subjectDTOS = new ArrayList<>();
            SubjectDTO subjectDTO;
            ToDoDTO toDoDTO;
            WeeksSubjectListDTO weeksSubjectListDTO;

            // 과목별로 subjectDTO List 생성
            for (int i=0; i<subjects.size(); i++) {
                List<String> subjectContentsTitles = new ArrayList<>();
                List<ToDoDTO> toDoDTOs = new ArrayList<>();
                // subjectContentsTitle List 생성
                for(SubjectContents subjectContent : subjectContents.get(i)) {
                    if(subjectContent.getWeek() == weeksInfo.getWeek())
                        subjectContentsTitles.add(subjectContent.getTitle());
                }
                // toDoDTO List 생성
                for(ToDo toDo : toDos.get(i)) {
                    if(toDo.getWeek() == weeksInfo.getWeek()) {
                        toDoDTO = ToDoDTO.builder()
                                .toDoId(toDo.getTodoId())
                                .content(toDo.getContent())
                                .isDone(toDo.getIsDone())
                                .build();
                        toDoDTOs.add(toDoDTO);
                    }
                }
                subjectDTO = SubjectDTO.builder()
                        .title(subjects.get(i).getSubjectName())
                        .subjectContentsTitle(subjectContentsTitles)
                        .toDoDTO(toDoDTOs)
                        .build();
                subjectDTOS.add(subjectDTO);
            }
            weeksSubjectListDTO = WeeksSubjectListDTO.builder()
                    .week(weeksInfo.getWeek())
                    .startDate(weeksInfo.getStartDate())
                    .endDate(weeksInfo.getEndDate())
                    .subjectDTO(subjectDTOS)
                    .build();
            weeksSubjectListDTOS.add(weeksSubjectListDTO);
        }

        // 과목별로 subjectNoticeListDTO List 생성
        for (int i=0; i<subjects.size(); i++) {
            SubjectNoticeDTO subjectNoticeDTO;
            List<SubjectNoticeDTO> subjectNoticeDTOS = new ArrayList<>();
            // subjectNoticeDTO List 생성
            for(SubjectNotice subjectNotice : subjectNotices.get(i)) {
                subjectNoticeDTO = SubjectNoticeDTO.builder()
                        .title(subjectNotice.getTitle())
                        .noticeLink(subjectNotice.getNoticeLink())
                        .build();
                subjectNoticeDTOS.add(subjectNoticeDTO);
            }
            SubjectNoticeListDTO subjectNoticeListDTO = SubjectNoticeListDTO.builder()
                    .title(subjects.get(i).getSubjectName())
                    .homepageAddress(subjects.get(i).getHomepageAddress())
                    .subjectNoticeDTO(subjectNoticeDTOS)
                    .build();
            subjectNoticeListDTOS.add(subjectNoticeListDTO);
        }

        SubjectListResponseDTO subjectListResponseDTO = SubjectListResponseDTO.builder()
                .weeksSubjectListDTO(weeksSubjectListDTOS)
                .subjectNoticeListDTO(subjectNoticeListDTOS)
                .build();

        return subjectListResponseDTO;
    }

    public Map addToDo(ToDoRequestDTO dto) {
        Map<String, Object> map = new HashMap<>();
        Optional<Attending> attending = attendingRepository.findAttendingByStudentIdAndSubjectId(dto.getStudentId(), dto.getSubjectId());
        // 수강하지 않는 과목인 경우
        if(attending.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Data");
        // 올바른 정보가 요청된 경우
        else {
            Optional<Student> student = studentRepository.findById(dto.getStudentId());
            Optional<Subject> subject = subjectRepository.findById(dto.getSubjectId());

            ToDo toDo = ToDo.builder()
                    .student(student.get())
                    .subject(subject.get())
                    .week(dto.getWeek())
                    .content(dto.getContent())
                    .build();
            toDoRepository.save(toDo);
            map.put("TodoId", toDo.getTodoId());
        }
        return map;
    }

    public Map deleteToDo(Long toDoId) {
        Map<String, Object> map = new HashMap<>();
        Optional<ToDo> toDoOptional = toDoRepository.findById(toDoId);
        // 삭제하고자 하는 데이터가 없는 경우
        if(toDoOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No Data");
        }
        // delete
        else {
            ToDo toDo = toDoOptional.get();
            if(toDo.getIsUsed()) { // is_used == true
                toDo.setIsUsed(false);
                toDoRepository.save(toDo);
                map.put("TodoId", toDo.getTodoId());
            }
            else // is_used == false
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No Data");
        }
        return map;
    }

    public Map checkToDo(Long toDoId) {
        Map<String, Object> map = new HashMap<>();
        Optional<ToDo> toDoOptional = toDoRepository.findById(toDoId);
        // 완료하고자 하는 데이터가 없는 경우
        if(toDoOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No Data");
        }
        // check
        else {
            ToDo toDo = toDoOptional.get();
            if(toDo.getIsUsed()) { // is_used == true
                toDo.setIsDone(true);
                toDoRepository.save(toDo);
                map.put("TodoId", toDo.getTodoId());
            }
            else // is_used == false
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No Data");
        }
        return map;
    }

    public Map deleteCheckToDo(Long toDoId) {
        Map<String, Object> map = new HashMap<>();
        Optional<ToDo> toDoOptional = toDoRepository.findById(toDoId);
        // 완료하고자 하는 데이터가 없는 경우
        if(toDoOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No Data");
        }
        // delete check
        else {
            ToDo toDo = toDoOptional.get();
            if(toDo.getIsUsed()) { // is_used == true
                if(toDo.getIsDone()) { // is_done == true
                    toDo.setIsDone(false);
                    toDoRepository.save(toDo);
                    map.put("TodoId", toDo.getTodoId());
                }
                else // is_done == false
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "NOT CHECKED STATE");
            }
            else // is_used == false
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No Data");
        }
        return map;
    }
}
