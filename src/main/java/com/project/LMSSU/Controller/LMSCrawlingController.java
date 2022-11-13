package com.project.LMSSU.Controller;

import com.project.LMSSU.DTO.LMSCrawlingRequestDTO;
import com.project.LMSSU.Service.LMSCrawlingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/subject")
public class LMSCrawlingController {
    private final LMSCrawlingService lmsCrawlingService;
    @GetMapping("/")
    public Map test() throws IOException, InterruptedException {
        LMSCrawlingRequestDTO dto = new LMSCrawlingRequestDTO((long)1, 20182662, "qwe@50584621");

        return lmsCrawlingService.saveLMSInformation(dto);
    }
}
