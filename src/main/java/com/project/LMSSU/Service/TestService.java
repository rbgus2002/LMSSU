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

    public void saveStudent1(){
        studentRepository.save(Student.builder()
                        .major(majorRepository.findById("컴퓨터학부").get())
                        .name("전종원")
                .build());
    }

    public void saveStudent2(){
        studentRepository.save(Student.builder()
                .major(majorRepository.findById("컴퓨터학부").get())
                .name("최규현")
            .build());
}

    public void saveMajor(){
        majorRepository.save(Major.builder()
                        .majorName("컴퓨터학부")
                        .homepageAddress("https://bitches")
                .build());
    }
}
