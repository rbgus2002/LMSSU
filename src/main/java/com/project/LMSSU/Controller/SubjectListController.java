package com.project.LMSSU.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/subject")
public class SubjectListController {
    @GetMapping()
    public String test(){
        return "subject";
    }
}
