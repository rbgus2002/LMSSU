package com.project.LMSSU.Controller;

import com.project.LMSSU.DTO.NoticeResponseDTO;
import com.project.LMSSU.Service.NoticeCrawlingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notice")
@Tag(name = "Notice Crawling Controller", description = "공지사항 컨트롤러")
public class NoticeCrawlingController {
    private final NoticeCrawlingService noticeCrawlingService;

    @Operation(summary = "[공지사항] 학교 공지사항 API", description = "크롤링을 통해 학교 공지사항 정보를 보내준다.(페이지 개수: 4, 한 페이지당 출력 항목: 6)")
    @GetMapping("/ssu")
    public NoticeResponseDTO sendSSUNoticeData(@RequestParam int page){
        return noticeCrawlingService.getSSUNoticeData(page);
    }

    @Operation(summary = "[공지사항] AI융합학부 공지사항 API", description = "크롤링을 통해 학과 공지사항 정보를 보내준다.(페이지 개수: 4, 한 페이지당 출력 항목: 6)")
    @GetMapping("/ai")
    public NoticeResponseDTO sendAINoticeData(@RequestParam int page){
        return noticeCrawlingService.getAINoticeData(page);
    }

    @Operation(summary = "[공지사항] 펀시스템 프로그램 API", description = "크롤링을 통해 펀시스템 프로그램 정보를 보내준다.(페이지 개수: 4, 한 페이지당 출력 항목: 6)")
    @GetMapping("/fun")
    public NoticeResponseDTO sendFunData(@RequestParam int page){
        return noticeCrawlingService.getFunData(page);
    }
}
