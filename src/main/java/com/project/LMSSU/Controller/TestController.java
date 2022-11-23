package com.project.LMSSU.Controller;

import com.project.LMSSU.Service.LMSCrawlingService;
import com.project.LMSSU.Service.StudentService;
import com.project.LMSSU.Service.TestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@Tag(name = "TestController", description = "테스트용 컨트롤러")
@RequiredArgsConstructor
@RestController
public class TestController {


    private final TestService testService;
    private final LMSCrawlingService lmsCrawlingService;

    @Operation(summary = "Test 메서드", description = "Test 메서드입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK !!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND !!"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
    })
    @GetMapping("/")
    public String test() {
        return "asdasdsadsa";
    }





}
