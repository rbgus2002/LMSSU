package com.project.LMSSU.Controller;

import com.project.LMSSU.DTO.LMSCrawlingRequestDTO;
import com.project.LMSSU.Service.LMSCrawlingService;
import com.project.LMSSU.Service.StudentService;
import com.project.LMSSU.Service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class TestController {
    private final TestService testService;
    private final LMSCrawlingService lmsCrawlingService;

    @GetMapping("/")
    public String test() {
        return "asdasdsadsa";
    }

    @GetMapping("/saveStudent1")
    public String saveStudent() {
        testService.saveStudent1();
        return "saveStudent1";
    }

    @GetMapping("/saveStudent2")
    public String saveStudent2() {
        testService.saveStudent2();
        return "saveStudent2";
    }

    @GetMapping("/saveMajor")
    public String saveMajor() {
        testService.saveMajor();
        return "saveMajor";
    }


    @GetMapping("/crawlTest")
    public Map crawlTest() throws IOException, InterruptedException {
        LMSCrawlingRequestDTO dto = new LMSCrawlingRequestDTO((long)1, 20182662, "qwe@50584621");

        return lmsCrawlingService.saveLMSInformation(dto);
    }
}
