package com.project.LMSSU.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/incomplete")
public class IncompleteTaskController {
    @GetMapping()
    public String test(){
        return "incomplete";
    }
}
