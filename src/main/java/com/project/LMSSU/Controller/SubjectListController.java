package com.project.LMSSU.Controller;

import com.project.LMSSU.DTO.SubjectListResponseDTO;
import com.project.LMSSU.DTO.ToDoRequestDTO;
import com.project.LMSSU.Service.SubjectListService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/list")
public class SubjectListController {
    private final SubjectListService subjectListService;

    @GetMapping()
    public SubjectListResponseDTO sendSubjectListData(@RequestParam Long studentId){
        return subjectListService.getSubjectListData(studentId);
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
