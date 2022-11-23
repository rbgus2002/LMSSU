package com.project.LMSSU.Controller;

import com.project.LMSSU.DTO.ExamScheduleRequestDTO;
import com.project.LMSSU.DTO.StudentLoginRequestDTO;
import com.project.LMSSU.DTO.StudentMajorAndNameRequestDTO;
import com.project.LMSSU.Service.StudentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@Controller
@RequestMapping("/student")
@Tag(name = "Student Controller", description = "학생 관련 컨트롤러")
public class StudentController {
    private final StudentService studentService;

    @PostMapping("/sign-in")
    public Map signIn(@RequestBody StudentLoginRequestDTO dto) throws InterruptedException {
        return studentService.signIn(dto);
    }

    @PostMapping("/sign-up")
    public Map signUp(@RequestBody StudentMajorAndNameRequestDTO dto){
        return studentService.updateMajorAndName(dto);
    }




//    public Map boardPrints(@RequestParam("regionName") String regionName){
//        return boardService.getBoardLists(regionName);
//    }
}
