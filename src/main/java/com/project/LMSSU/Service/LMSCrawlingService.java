package com.project.LMSSU.Service;

import com.project.LMSSU.DTO.StudentLoginRequestDTO;
import com.project.LMSSU.Entity.*;
import com.project.LMSSU.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.swing.text.DateFormatter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static java.time.format.DateTimeFormatter.ofPattern;

@RequiredArgsConstructor
@Service
public class LMSCrawlingService {

    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private final MajorRepository majorRepository;
    private final AttendingRepository attendingRepository;
    private final SubjectContentsRepository subjectContentsRepository;
    private final SubjectNoticeRepository subjectNoticeRepository;

    /*
    크롤링 정보 DB 저장
     */
//    public Map saveLMSInformation(LMSCrawlingRequestDTO lmsCrawlingRequestDTO) throws IOException, InterruptedException {
//        // Student 예외처리
//        Optional<Student> student = studentRepository.findById(lmsCrawlingRequestDTO.getStudentId());
//        if (student.isEmpty()) {
//            System.out.println("studentId Error"); // 로그로 찍기
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "studentId error");
//        }
//
//        // LMS 크롤링
//        crawlingSubjectList = LMSCrawling.run(lmsCrawlingRequestDTO.getUserId(), lmsCrawlingRequestDTO.getPwd());
//
//        for (CrawlingSubject subject : crawlingSubjectList) {
//            System.out.println("과목 이름 : " + subject.getName());
//
//            // subject 저장
//            System.out.println("Subject Table 저장");
//            Optional<Subject> optionalSubject = subjectRepository.findBySubjectName(subject.getName());
//            Subject newSubject;
//            if (optionalSubject.isEmpty()) {
//                newSubject = subjectRepository.save(Subject.builder()
//                        .subjectName(subject.getName())
//                        .professorName(subject.getProfessor())
//                        .homepageAddress(subject.getHomepageLink())
//                        .build());
//
//            }else{
//                newSubject = optionalSubject.get();
//            }
//
//            // attending Table 저장
//            System.out.println("Attending Table 저장");
//            attendingRepository.save(Attending.builder()
//                            .studentId(student.get())
//                            .subjectId(newSubject)
//                    .build());
//
//
//            // subject_contents Table 저장
//            // subject_id에 해당하는 tuple 삭제 후 저장 (FK Attribute cascade 삭제)
//            for(ContentPerWeek contentPerWeek : subject.getContentPerWeekList()){
//                for(Content content : contentPerWeek.getContentList()){
//                    //LocalDate 객체 생성
//                    LocalDate localDate = null;
//                    StringTokenizer st;
//                    String contentEndDate = content.getEndDate();
//                    if(!contentEndDate.equals("None")){
//                        st = new StringTokenizer(contentEndDate, " ");
//                        Integer year = LocalDate.now().getYear();
//                        String month = st.nextToken().replaceAll("[^0-9]", ""); // 숫자만 추출
//                        System.out.println("month : " + month);
//                        String day = st.nextToken().replaceAll("[^0-9]", "");
//                        localDate = LocalDate.of(year, Integer.parseInt(month), Integer.parseInt(day));
//
//                        System.out.println("localdate : " + localDate.toString());
//                    }
//
//                    subjectContentsRepository.save(SubjectContents.builder()
//                                    .contentType(content.getType())
//                                    .endDate(localDate)
//                                    .sequence(1)
//                                    .title(content.getName())
//                                    .week(contentPerWeek.getWeeks())
//                                    .subject(newSubject)
//                            .build());
//                }
//            }
//
//        }
//
//        // Map 리턴
//        Map<String, Object> map = new HashMap<>();
//        map.put("studentId", lmsCrawlingRequestDTO.getStudentId());
//        return map;
//    }




    /*
    특정 과목 Crawling
     */
//    @Transactional
//    public Map saveSubjectInfo(StudentLoginRequestDTO lmsCrawlingRequestDTO, Long subjectId) throws InterruptedException {
//        // Student 예외처리
//        Optional<Student> student = studentRepository.findById(lmsCrawlingRequestDTO.getStudentId());
//        if (student.isEmpty()) {
//            System.out.println("studentId Error"); // 로그로 찍기
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "studentId error");
//        }
//
//        // Subject 예외처리
//        Optional<Subject> subjectOptional = subjectRepository.findById(subjectId);
//        if (subjectOptional.isEmpty()) {
//            System.out.println("subjectId Error"); // 로그로 찍기
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "subjectId error");
//        }
//        Subject subject = subjectOptional.get();
//
//        // Attending 예외처리
//        Optional<Attending> attendingOptional = attendingRepository.findAttendingByStudentIdAndSubjectId(lmsCrawlingRequestDTO.getStudentId(), subjectId);
//        if (attendingOptional.isEmpty()) {
//            System.out.println("Attending Error"); // 로그로 찍기
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "not attending subjectId");
//        }
//
//        // LMSCrawling 객체 생성 (init, login)
//        LMSCrawling lmsCrawling = new LMSCrawling(lmsCrawlingRequestDTO);
//
//        // subjectId에 해당하는 과목의 강의 컨텐츠, 공지사항 가져오기
//        SubjectContentsInfo subjectContentsInfo = lmsCrawling.getSubjectContentsInfo(subject.getHomepageAddress());
//
//        // 강의콘텐츠 저장
////        for(ContentPerWeek contentPerWeek : subjectContentsInfo.getContentPerWeekList()){
////            for(Content content : contentPerWeek.getContentList()){
////                LocalDate date = null;
////                // localDate 초기화
////                if(!content.getEndDate().equals("None")){
////                    String year = LocalDate.now().getYear() + " ";
////                    String yearTmp = year + content.getEndDate();
////                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy M월 d일 m:s");
////                    date = LocalDate.parse(yearTmp, formatter);
////                }
////
////                subjectContentsRepository.save(SubjectContents.builder()
////                                .contentType(content.getType())
////                                .endDate(date)
////                                .title(content.getName())
////                                .week(contentPerWeek.getWeeks())
////                                .subject(subject)
////                        .build());
////            }
////        }
//
//        // 과목 공지사항 저장
//        for (Notice notice : subjectContentsInfo.getNoticeList()) {
//            // localDate 초기화
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 M월 d일 오후 h:m");
//            LocalDate date = LocalDate.parse(notice.getDate(), formatter);
//
//            System.out.println(date);
//
//            subjectNoticeRepository.save(SubjectNotice.builder()
//                    .title(notice.getTitle())
//                    .localDate(date)
//                    .subject(subject)
//                    .noticeLink(notice.getLink())
//                    .subjectNoticeId(notice.getNoticeId())
//                    .build());
//        }
//
//        // Subject TABLE update_time 갱신
//        subject.setUpdateTime(LocalDateTime.now());
//        subjectRepository.save(subject);
//
//        lmsCrawling.quitCrawling();
//
//        // return
//        Map map = new HashMap();
//        map.put("subjectId", subjectId);
//        return map;
//    }
}
/*
과목공지 : [Notice{title='설계과제 1,2 이의신청 반영된 점수 공지', date='2022년 11월 22일 오후 12:15', link='https://canvas.ssu.ac.kr/courses/16648/discussion_topics/75225'}, Notice{title='[필독] 설계과제3 채점 결과 및 이의신청방법 공지', date='2022년 11월 21일 오후 7:24', link='https://canvas.ssu.ac.kr/courses/16648/discussion_topics/75111'}, Notice{title='중간고사 채점 결과', date='2022년 11월 17일 오후 4:28', link='https://canvas.ssu.ac.kr/courses/16648/discussion_topics/74483'}, Notice{title='OS 기말고사 일정 (12월12일 월요일 저녁 7시 - 9시) 공지', date='2022년 11월 15일 오후 4:41', link='https://canvas.ssu.ac.kr/courses/16648/discussion_topics/74122'}, Notice{title='설계과제 1, 2 채점 결과 공지', date='2022년 10월 31일 오후 6:25', link='https://canvas.ssu.ac.kr/courses/16648/discussion_topics/71596'}, Notice{title='OS 중간고사 일정 (10월24일 월요일 저녁 7시 - 9시) 공지', date='2022년 9월 27일 오후 7:06', link='https://canvas.ssu.ac.kr/courses/16648/discussion_topics/64805'}, Notice{title='OS 과제 관련 구글클래스룸 참여 및 과제1 확인 (제출기한: 9/12(월) 밤 12시 전까지)', date='2022년 8월 30일 오후 12:35', link='https://canvas.ssu.ac.kr/courses/16648/discussion_topics/58464'}]
1 : [pdf  '2022-2 SSU CSE OS Syllabus 라분반' / None / '9월 7일 23:59', pdf  '강의자료01' / None / '9월 7일 23:59', mp4  '01. OS 녹화영상' / 출석 / '9월 7일 23:59', offline_attendance  '2차시 대면강의 09/06(화) 15:00' / 출석 / 'None', pdf  '참고자료 (리눅스 프로그래밍 기초)' / None / '9월 7일 23:59', mp4  '참고영상 (리눅스 프로그래밍 기초) - 출석 반영되지 않는 영상 - 필요한 학생들만 듣기 바람' / None / '9월 7일 23:59']
2 : [pdf  '강의자료02' / None / '9월 14일 23:59', mp4  '02. OS 녹화영상' / 출석 / '9월 14일 23:59', pdf  '강의자료03' / None / '9월 14일 23:59', offline_attendance  '2차시 대면강의 09/13(화) 15:00' / 출석 / 'None']
3 : [pdf  '강의자료03' / None / '9월 21일 23:59', mp4  '03. OS 녹화영상' / 출석 / '9월 21일 23:59', offline_attendance  '2차시 대면강의 09/20(화) 15:00' / 출석 / 'None']
4 : [pdf  '강의자료04' / None / '9월 28일 23:59', mp4  '04. OS 녹화영상' / 출석 / '9월 28일 23:59', offline_attendance  '2차시 대면강의 09/27(화) 15:00' / 출석 / 'None']
5 : [mp4  '05. OS 녹화영상' / 출석 / '10월 5일 23:59', pdf  '참고자료 CFS 스케줄링' / None / '10월 5일 23:59', offline_attendance  '2차시 대면강의 10/04(화) 15:00' / 출석 / 'None', mp4  '참고영상 (10월4일 대면강의) - 출석 반영되지 않는 영상임' / None / '10월 5일 23:59']
6 : [pdf  '강의자료05' / None / '10월 12일 23:59', mp4  '06. OS 녹화영상' / 출석 / '10월 12일 23:59', offline_attendance  '2차시 대면강의 10/11(화) 15:00' / 출석 / 'None']
7 : [mp4  '07. OS 녹화영상' / 출석 / '10월 19일 23:59', pdf  '강의자료06' / None / '10월 19일 23:59', offline_attendance  '2차시 대면강의 10/18(화) 15:00' / 출석 / 'None']
8 : [offline_attendance  '중간고사' / 출석 / 'None', offline_attendance  '중간고사' / 출석 / 'None', pdf  '강의자료06' / None / '10월 26일 23:59']
9 : [mp4  '09. OS 녹화영상' / 출석 / '11월 2일 23:59', offline_attendance  '2차시 11/01(화) 15:00' / 출석 / 'None', mp4  '참고영상 (11월1일 대면강의) - 출석 반영되지 않는 영상임' / None / '11월 2일 23:59', pdf  '과제4 참고자료' / None / '11월 2일 23:59', pdf  '강의자료07' / None / '11월 2일 23:59']
10 : [mp4  '10. OS 녹화영상' / 출석 / '11월 9일 23:59', pdf  '참고자료 (Pthreads)' / None / '11월 9일 23:59', mp4  '참고영상 (Pthreads) - 출석 반영되지 않는 영상임' / None / '11월 9일 23:59', pdf  '강의자료08' / None / '11월 9일 23:59', offline_attendance  '2차시 11/08(화) 15:00' / 출석 / 'None']
11 : [pdf  '강의자료09' / None / '11월 16일 23:59', mp4  '11. OS 녹화영상' / 출석 / '11월 16일 23:59', pdf  '강의자료10' / None / '11월 16일 23:59', offline_attendance  '2차시 11/15(화) 15:00' / 출석 / 'None']
12 : [pdf  '강의자료11' / None / '11월 23일 23:59', mp4  '12. OS 녹화영상' / 출석 / '11월 23일 23:59', offline_attendance  '2차시 11/22(화) 15:00' / - / 'None', pdf  '강의자료12' / None / '11월 23일 23:59']
------------------------------------------
 */