package com.project.LMSSU.Controller;

import com.project.LMSSU.DTO.StudentLoginRequestDTO;
import com.project.LMSSU.DTO.SubjectListResponseDTO;
import com.project.LMSSU.DTO.ToDoRequestDTO;
import com.project.LMSSU.Service.SubjectListService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/list")
@Tag(name = "SubjectList Controller", description = "과목 리스트 컨트롤러")
public class SubjectListController {
    private final SubjectListService subjectListService;

    @GetMapping()
    public SubjectListResponseDTO sendSubjectListData(@RequestParam Long studentId, @RequestParam Integer week){
        return subjectListService.getSubjectListData(studentId, week);
    }

    @Operation(summary = "[과목 리스트] 특정 주차의 과목 리스트를 불러온다.", description = "크롤링이 필요한 과목은 크롤링 후에 과목 리스트를 불러온다. ")
    @PostMapping ()
    public Map getSubjectInfoList(@RequestBody StudentLoginRequestDTO dto, @RequestParam Integer week) throws InterruptedException {
        return subjectListService.getLMSInfo(dto, week);
    }

    @Operation(summary = "[과목 리스트] To-Do-List 추가 API", description = "DB에 To-Do-List를 추가한다.")
    @PostMapping("/todo")
    public Map addToDo(@RequestBody ToDoRequestDTO dto){
        return subjectListService.addToDo(dto);
    }

    @Operation(summary = "[과목 리스트] To-Do-List 삭제 API", description = "입력받은 To-Do-List와 일치하는 정보가 있다면 삭제시킨다.(isUsed -> false)")
    @GetMapping("/todo/cancel")
    public Map cancelToDo(@RequestParam Long toDoId){
        return subjectListService.deleteToDo(toDoId);
    }

    @Operation(summary = "[과목 리스트] To-Do-List 체크 API", description = "입력받은 To-Do-List와 일치하는 정보가 있다면 isDone -> true로 설정한다.")
    @GetMapping("/todo/check")
    public Map checkToDo(@RequestParam Long toDoId){
        return subjectListService.checkToDo(toDoId);
    }

    @Operation(summary = "[과목 리스트] To-Do-List 체크 해제 API", description = "입력받은 To-Do-List와 일치하는 정보가 있다면 isDone -> false로 설정한다.")
    @GetMapping("/todo/check/cancel")
    public Map cancelCheckToDo(@RequestParam Long toDoId){
        return subjectListService.deleteCheckToDo(toDoId);
    }
}
