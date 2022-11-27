package com.project.LMSSU.Controller;

import com.project.LMSSU.DTO.CalendarResponseDTO;
import com.project.LMSSU.DTO.ExamScheduleDTO;
import com.project.LMSSU.DTO.ExamScheduleRequestDTO;
import com.project.LMSSU.Service.CalendarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/calendar")
@Tag(name = "Claendar Controller", description = "캘린더 컨트롤러")
public class CalendarController {
    private final CalendarService calendarService;

    @Operation(summary = "[캘린더] DB에 저장된 데이터 불러오는 API", description = "웹 페이지 접속 시에 DB에 저장되어 있는 정보를 보내준다.")
    @GetMapping()
    public CalendarResponseDTO sendCalendarData(@RequestParam Long studentId){
        return calendarService.getCalendarData(studentId);
    }
    @Operation(summary = "[캘린더] 시험 일정 추가 API", description = "DB에 시험 일정을 추가한다.")
    @PostMapping("/exam")
    public Map addExamSchedule(@RequestBody ExamScheduleRequestDTO dto){
        return calendarService.addExamSchedule(dto);
    }
    @Operation(summary = "[캘린더] 시험 일정 삭제 API", description = "입력받은 시험 일정과 일치하는 정보가 있다면 삭제시킨다.(isUsed -> false)")
    @PostMapping("/exam/cancel")
    public Map cancelExamSchedule(@RequestBody ExamScheduleRequestDTO dto){
        return calendarService.deleteExamSchedule(dto);
    }

}
