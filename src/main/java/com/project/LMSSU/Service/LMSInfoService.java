package com.project.LMSSU.Service;

import com.project.LMSSU.DTO.StudentLoginRequestDTO;
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

@RequiredArgsConstructor
@Service
public class LMSInfoService {
    private final AttendingRepository attendingRepository;
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private final SubjectNoticeRepository subjectNoticeRepository;
    private final MajorRepository majorRepository;
    private final SubjectContentsRepository subjectContentsRepository;

    /*
    특정 주차에 해당하는 과목정보 리스트를 불러온다.
     */
    public Map getLMSInfo(StudentLoginRequestDTO dto) throws InterruptedException {
        // Student 예외처리
        Optional<Student> student = studentRepository.findById(dto.getStudentId());
        if (student.isEmpty()) {
            System.out.println("studentId Error"); // 로그로 찍기
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

        System.out.println(updateSubjectList.toString());
        // 크롤링하여 정보 업데이트
        System.out.println("go saveSubjectInfo");
        saveSubjectInfo(dto, updateSubjectList);

        // 이제 DB 뒤져서 내용 적당히 뽑아서 return 해야 해

        Map map = new HashMap();
        map.put("test", 1);
        return map;
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
                        if(content.getName().contains(subjectContents.getTitle())){
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
                    subjectContentsRepository.save(SubjectContents.builder()
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
}
