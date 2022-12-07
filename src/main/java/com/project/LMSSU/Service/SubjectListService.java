package com.project.LMSSU.Service;

import com.project.LMSSU.DTO.*;
import com.project.LMSSU.Entity.*;
import com.project.LMSSU.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
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

    /*
    특정 주차에 해당하는 과목정보 리스트를 불러온다.
     */
    public SubjectListResponseDTO getLMSInfo(StudentLoginRequestDTO dto, Integer week) throws InterruptedException {
        // Student 예외처리
        Optional<Student> student = studentRepository.findById(dto.getStudentId());
        if (student.isEmpty()) {
            System.out.println("studentId Error");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "studentId error");
        }

        // studentId로 수강 중인 과목id List 가져오기
        List<Subject> subjectIdList = attendingRepository.findSubjectByStudentId(dto.getStudentId());

        // 크롤링 필요한 subject 추출
        List<Subject> updateSubjectList = new ArrayList<>();
        for (Subject subject : subjectIdList) {
            // 한 번도 크롤링 해준 적 없는 과목
            if (subject.getUpdateTime() == null) {
                updateSubjectList.add(subject);
                continue;
            }

            // 2시간이 지난 과목
            if (ChronoUnit.HOURS.between(subject.getUpdateTime(), LocalDateTime.now()) > 2) {
                updateSubjectList.add(subject);
            }
        }

        // 크롤링하여 정보 업데이트
        saveSubjectInfo(dto, updateSubjectList);

        // week에 해당하는 LMS 정보 return
        SubjectListResponseDTO subjectListResponseDTO = getSubjectListData(dto.getStudentId(), week);
        return subjectListResponseDTO;
    }


    /*
    특정 과목 Crawling
    */
    @Transactional
    public void saveSubjectInfo(StudentLoginRequestDTO lmsCrawlingRequestDTO, List<Subject> subjectList) throws InterruptedException {
        if(subjectList.isEmpty()){
            System.out.println("크롤링 할 과목 존재 X");
            return;
        }

        // Student 예외처리
        Optional<Student> student = studentRepository.findById(lmsCrawlingRequestDTO.getStudentId());
        if (student.isEmpty()) {
            System.out.println("studentId Error"); // 로그로 찍기
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "studentId error");
        }

        // LMSCrawling 객체 생성 (init, login)
        LMSCrawling lmsCrawling = new LMSCrawling(lmsCrawlingRequestDTO);

        // 리스트에 담겨있는 과목 크롤링
        for (Subject subject : subjectList) {
            // Attending 예외처리
            Optional<Attending> attendingOptional = attendingRepository.findAttendingByStudentIdAndSubjectId(lmsCrawlingRequestDTO.getStudentId(), subject.getId());
            if (attendingOptional.isEmpty()) {
                System.out.println("Attending Error"); // 로그로 찍기
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "not attending subjectId");
            }

            // subjectId에 해당하는 과목의 강의 컨텐츠, 공지사항 가져오기
            SubjectContentsInfo subjectContentsInfo = lmsCrawling.getSubjectContentsInfo(subject.getHomepageAddress());

            System.out.println(subject.getSubjectName());
            System.out.println(subjectContentsInfo.toString());

            // 강의콘텐츠 저장
            for (ContentPerWeek contentPerWeek : subjectContentsInfo.getContentPerWeekList()) {
                // DB에서 subject_id랑 week로 query 하기 -> 개수 체크
                List<SubjectContents> subjectContentsList = subjectContentsRepository.findSubjectContentsBySubjectIdAndWeek(subject.getId(), contentPerWeek.getWeeks());
                int dbSubjectContentsNum = subjectContentsList.size();
                int crawlingSubjectContentsNum = contentPerWeek.getContentList().size();

                // 현재 loop의 개수와 디비 개수 같으면 continue
                if(dbSubjectContentsNum == crawlingSubjectContentsNum){
                    continue;
                }

                // 강의콘텐츠 loop
                for (Content content : contentPerWeek.getContentList()) {
                    // 이미 DB에 존재하면 continue
                    boolean flag = false;
                    for(SubjectContents subjectContents : subjectContentsList){
                        // 제목으로 존재하는 지 비교
                        if(content.getName().contains(subjectContents.getTitle())) {
                            flag = true;
                            break;
                        }
                    }
                    if(flag)
                        continue;

                    // localDate 초기화
                    LocalDate date = null;
                    if (!content.getEndDate().equals("None")) {
                        String year = LocalDate.now().getYear() + " ";
                        String yearTmp = year + content.getEndDate();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy M월 d일 m:s");
                        date = LocalDate.parse(yearTmp, formatter);
                    }

                    // 강의컨텐츠 저장
                    SubjectContents subjectContents = subjectContentsRepository.save(SubjectContents.builder()
                            .contentType(content.getType())
                            .endDate(date)
                            .title(content.getName())
                            .week(contentPerWeek.getWeeks())
                            .subject(subject)
                            .build());
                }
            }

            // 과목 공지사항 저장
            for (Notice notice : subjectContentsInfo.getNoticeList()) {
                // localDate 초기화
                DateTimeFormatter formatter = null;
                if(notice.getDate().toString().contains("오후")){
                    formatter = DateTimeFormatter.ofPattern("yyyy년 M월 d일 오후 h:m");
                }else if(notice.getDate().equals("")){
                    continue;
                }
                else{
                    formatter = DateTimeFormatter.ofPattern("yyyy년 M월 d일 오전 h:m");
                }

                LocalDate date = LocalDate.parse(notice.getDate(), formatter);

                subjectNoticeRepository.save(SubjectNotice.builder()
                        .title(notice.getTitle())
                        .localDate(date)
                        .subject(subject)
                        .noticeLink(notice.getLink())
                        .subjectNoticeId(notice.getNoticeId())
                        .build());
            }

            // Subject TABLE update_time 갱신
            subject.setUpdateTime(LocalDateTime.now());
            subjectRepository.save(subject);
        }

        // 드라이버 종료
        lmsCrawling.quitCrawling();
    }

    /*
    주차를 입력받아 해당 주차에 해당하는 정보를 보내준다.
     */
    public SubjectListResponseDTO getSubjectListData(Long studentId, Integer week) {
        List<Attending> attendings = attendingRepository.findAttendingByStudentId(studentId);
        List<Subject> subjects = new ArrayList<>();
        List<List<SubjectContents>> subjectContents = new ArrayList<>();
        List<List<SubjectNotice>> subjectNotices = new ArrayList<>();
        List<List<ToDo>> toDos = new ArrayList<>();
        Optional<WeeksInfo> weeksInfoOptional = weeksInfoRepository.findById(week);
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

        if (weeksInfoOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "weeksInfo error");
        }
        WeeksInfo weeksInfo = weeksInfoOptional.get();

        // week에 해당하는 weeksSubjectListDTO List 생성
        List<SubjectDTO> subjectDTOS = new ArrayList<>();
        SubjectDTO subjectDTO;
        ToDoDTO toDoDTO;
        WeeksSubjectListDTO weeksSubjectListDTO;

        // 과목별로 subjectDTO List 생성
        for (int i=0; i<subjects.size(); i++) {
            List<SubjectContentsDTO> subjectContentsTitles = new ArrayList<>();
            List<ToDoDTO> toDoDTOs = new ArrayList<>();
            // subjectContentsTitle List 생성
            for(SubjectContents subjectContent : subjectContents.get(i)) {
                if(subjectContent.getWeek() == weeksInfo.getWeek()) {
                    SubjectContentsDTO subjectContentsDTO;
                    subjectContentsDTO = SubjectContentsDTO.builder()
                            .title(subjectContent.getTitle())
                            .contentsType(subjectContent.getContentType())
                            .build();
                    subjectContentsTitles.add(subjectContentsDTO);
                }
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
                .startDate(weeksInfo.getStartDate())
                .endDate(weeksInfo.getEndDate())
                .subjectDTO(subjectDTOS)
                .build();

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
                .weeksSubjectListDTO(weeksSubjectListDTO)
                .subjectNoticeListDTO(subjectNoticeListDTOS)
                .build();

        return subjectListResponseDTO;
    }


    /*
    과목별 할 일을 추가함
     */
    public Map addToDo(ToDoRequestDTO dto) {
        Map<String, Object> map = new HashMap<>();
        Optional<Attending> attending = attendingRepository.findAttendingByStudentIdAndSubjectId(dto.getStudentId(), dto.getSubjectId());
        List<WeeksInfo> weeksInfos = weeksInfoRepository.findAll();
        WeeksInfo firstWeek = weeksInfos.get(0);
        WeeksInfo endWeek = weeksInfos.get(weeksInfos.size()-1);
        // 수강하지 않는 과목인 경우
        if(attending.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Data");
        // 올바른 정보가 요청된 경우
        else {
            Optional<Student> student = studentRepository.findById(dto.getStudentId());
            Optional<Subject> subject = subjectRepository.findById(dto.getSubjectId());
            if(dto.getWeek() < firstWeek.getWeek() || dto.getWeek() > endWeek.getWeek()) // 입력받은 주차가 1~15주차가 아닌 경우 처리
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong Weeks");
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
    /*
    입력받은 To-Do-List 삭제
     */
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
    /*
    입력받은 To-Do-List 완료로 설정 (isDone->true)
     */
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
    /*
    입력받은 To-Do-List 완료 해제 (isDone->false)
     */
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
