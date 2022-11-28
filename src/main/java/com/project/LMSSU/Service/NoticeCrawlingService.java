package com.project.LMSSU.Service;

import com.project.LMSSU.DTO.NoticeDTO;
import com.project.LMSSU.DTO.NoticeResponseDTO;
import com.project.LMSSU.Entity.Student;
import com.project.LMSSU.Repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NoticeCrawlingService {
    final int recordSize = 6; // 페이지당 출력할 데이터 개수
    final int pageSize = 4; // 총 페이지 수
    private final StudentRepository studentRepository;
    NoticeCrawling noticeCrawling = new NoticeCrawling();

    /*
    학교 공지사항 크롤링하고 페이지 개수에 맞게 데이터 보내준다.
    */
    public NoticeResponseDTO getSSUNoticeData(int page) {
        List<NoticeDTO> noticeDTOS = new ArrayList<>();
        List<NoticeDTO> data = noticeCrawling.ssuNoticeCrawling();
        if(page > pageSize || page < 1)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "wrong page");
        // 입력받은 페이지에 대한 정보 추출
        for(int idx = (page-1) * recordSize; idx < page * recordSize; idx++) {
            NoticeDTO noticeDTO = data.get(idx);
            noticeDTOS.add(noticeDTO);
        }

        NoticeResponseDTO noticeResponseDTO = NoticeResponseDTO.builder()
                .page(page)
                .ssuNoticeDTO(noticeDTOS)
                .build();

        return noticeResponseDTO;
    }

    /*
    펀시스템 프로그램 정보 크롤링하고 페이지 개수에 맞게 데이터 보내준다.
    */
    public NoticeResponseDTO getFunData(int page) {
        List<NoticeDTO> noticeDTOS = new ArrayList<>();
        List<NoticeDTO> data = noticeCrawling.funProgramCrawling();
        if(page > pageSize || page < 1)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "wrong page");
        // 입력받은 페이지에 대한 정보 추출
        for(int idx = (page-1) * recordSize; idx < page * recordSize; idx++) {
            NoticeDTO noticeDTO = data.get(idx);
            noticeDTOS.add(noticeDTO);
        }

        NoticeResponseDTO noticeResponseDTO = NoticeResponseDTO.builder()
                .page(page)
                .ssuNoticeDTO(noticeDTOS)
                .build();

        return noticeResponseDTO;
    }

    /*
    학생의 학과 공지사항 크롤링하고 페이지 개수에 맞게 데이터 보내준다.
    */
    public NoticeResponseDTO getNoticeData(Long studentId, int page) {
        List<NoticeDTO> noticeDTOS = new ArrayList<>();
        List<NoticeDTO> data = new ArrayList<>();
        Optional<Student> studentOptional = studentRepository.findById(studentId);
        if(studentOptional.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Data");
        if(page > pageSize || page < 1)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "wrong page");
        switch (studentOptional.get().getMajor().getMajorName()) {
            case "AI융합학부" :
                data = noticeCrawling.aiNoticeCrawling();
                break;
            case "글로벌미디어학부" :
                data = noticeCrawling.mediaNoticeCrawling();
                break;
            case "컴퓨터학부" :
                data = noticeCrawling.csNoticeCrawling();
                break;
            case "소프트웨어학부" :
                data = noticeCrawling.swNoticeCrawling();
                break;
        }
        // 입력받은 페이지에 대한 정보 추출
        for(int idx = (page-1) * recordSize; idx < page * recordSize; idx++) {
            NoticeDTO noticeDTO = data.get(idx);
            noticeDTOS.add(noticeDTO);
        }

        NoticeResponseDTO noticeResponseDTO = NoticeResponseDTO.builder()
                .page(page)
                .ssuNoticeDTO(noticeDTOS)
                .build();

        return noticeResponseDTO;
    }
}
