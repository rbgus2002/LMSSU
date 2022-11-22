package com.project.LMSSU.Controller;

import com.project.LMSSU.DTO.CalendarResponseDTO;
import com.project.LMSSU.DTO.ExamScheduleDTO;
import com.project.LMSSU.DTO.ExamScheduleRequestDTO;
import com.project.LMSSU.Service.CalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/calendar")
public class CalendarController {
    private final CalendarService calendarService;

    @GetMapping()
    public CalendarResponseDTO sendCalendarData(@RequestParam Long studentId){
        return calendarService.getCalendarData(studentId);
    }
    @PostMapping("/exam")
    public Map addExamSchedule(@RequestBody ExamScheduleRequestDTO dto){
        return calendarService.addExamSchedule(dto);
    }
    @PostMapping("/exam/cancel")
    public Map cancelExamSchedule(@RequestBody ExamScheduleRequestDTO dto){
        return calendarService.deleteExamSchedule(dto);
    }

}
