package com.project.LMSSU.Controller;

import com.project.LMSSU.DTO.StudentLoginRequestDTO;
import com.project.LMSSU.DTO.SubjectListResponseDTO;
import com.project.LMSSU.DTO.ToDoRequestDTO;
import com.project.LMSSU.Service.SubjectListService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/list")
public class SubjectListController {
    private final SubjectListService subjectListService;

//    @GetMapping()
//    public SubjectListResponseDTO sendSubjectListData(@RequestParam Long studentId){
//        return subjectListService.getSubjectListData(studentId);
//    }

    @Operation(summary = "[과목 리스트] 특정 주차의 과목 리스트를 불러온다.", description = "크롤링이 필요한 과목은 크롤링 후에 과목 리스트를 불러온다. ")
    @PostMapping ()
    public Map getSubjectInfoList(@RequestBody StudentLoginRequestDTO dto, @RequestParam int week) throws InterruptedException {
        return subjectListService.getLMSInfo(dto, week);
    }

    @PostMapping("/todo")
    public Map addToDo(@RequestBody ToDoRequestDTO dto){
        return subjectListService.addToDo(dto);
    }

    @GetMapping("/todo/cancel")
    public Map cancelToDo(@RequestParam Long toDoId){
        return subjectListService.deleteToDo(toDoId);
    }

    @GetMapping("/todo/check")
    public Map checkToDo(@RequestParam Long toDoId){
        return subjectListService.checkToDo(toDoId);
    }

    @GetMapping("/todo/check/cancel")
    public Map cancelCheckToDo(@RequestParam Long toDoId){
        return subjectListService.deleteCheckToDo(toDoId);
    }
}
