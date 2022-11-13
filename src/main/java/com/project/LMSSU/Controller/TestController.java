package com.project.LMSSU.Controller;

import com.project.LMSSU.Service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TestController {
    private final StudentService studentService;

    @GetMapping("/")
    public String test() {
        return "asdasdsadsa";
    }

    @GetMapping("/saveTmpStudent")
    public String test2() {
        studentService.test();

        return "d";
    }
}
