package com.project.LMSSU.Controller;

import com.project.LMSSU.Service.StudentService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class StudentController {
    private final StudentService studentService;

}
