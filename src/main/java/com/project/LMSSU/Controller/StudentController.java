package com.project.LMSSU.Controller;

import com.project.LMSSU.DTO.ExamScheduleRequestDTO;
import com.project.LMSSU.DTO.StudentLoginRequestDTO;
import com.project.LMSSU.DTO.StudentMajorAndNameRequestDTO;
import com.project.LMSSU.Service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/student")
@Tag(name = "Student Controller", description = "학생 관련 컨트롤러")
public class StudentController {
    private final StudentService studentService;

    @Operation(summary = "로그인 API", description = "student TABLE에 학생이 등록되어 있는 지 체크한다. student가 new면 회원가입 API 호출해야 한다. (이름&학과 미등록)")
    @PostMapping("/sign-in")
    public Map signIn(@RequestBody StudentLoginRequestDTO dto) throws InterruptedException {
        return studentService.signIn(dto);
    }

    @Operation(summary = "회원가입 API", description = "학과와 이름을 등록한다.")
    @PostMapping("/sign-up")
    public Map signUp(@RequestBody StudentMajorAndNameRequestDTO dto){
        return studentService.updateMajorAndName(dto);
    }






//    public Map boardPrints(@RequestParam("regionName") String regionName){
//        return boardService.getBoardLists(regionName);
//    }
}
