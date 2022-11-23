package com.project.LMSSU.Service;

import com.project.LMSSU.Entity.Major;
import com.project.LMSSU.Entity.Student;
import com.project.LMSSU.Repository.MajorRepository;
import com.project.LMSSU.Repository.StudentRepository;
import com.project.LMSSU.Repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TestService {
    private final StudentRepository studentRepository;
    private final MajorRepository majorRepository;
    private final SubjectRepository subjectRepository;

}
