package com.project.LMSSU.Controller;

import com.project.LMSSU.DTO.StudentLoginRequestDTO;
import com.project.LMSSU.Repository.SubjectRepository;
import com.project.LMSSU.Service.TestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;


@RequiredArgsConstructor
@RestController
public class TestController {


    private final TestService testService;
    private final SubjectRepository  subjectRepository;


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

    @GetMapping("/weeks")
    public void addWeeks() {
        testService.addWeeks();
    }

    @GetMapping("/time")
    public Map testTime() {
        Map map = new HashMap();
        map.put("currentTime", LocalDateTime.now());
        map.put("opensourceUpdateTime", subjectRepository.findById(2150061301L).get().getUpdateTime());
        map.put("differenceTime", ChronoUnit.HOURS.between(subjectRepository.findById(2150061301L).get().getUpdateTime(), LocalDateTime.now()));

        return map;
    }
}
