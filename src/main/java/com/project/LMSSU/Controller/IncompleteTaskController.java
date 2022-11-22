package com.project.LMSSU.Controller;

import com.project.LMSSU.DTO.IncompleteTaskResponseDTO;
import com.project.LMSSU.Service.IncompleteTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/incomplete")
public class IncompleteTaskController {
    private IncompleteTaskService incompleteTaskService;
//    @GetMapping()
//    public IncompleteTaskResponseDTO sendData(@RequestParam Long studentId){
//        return incompleteTaskService.getData(studentId);
//    }
}
