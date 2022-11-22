package com.project.LMSSU.Service;

import com.project.LMSSU.DTO.IncompleteTaskDTO;
import com.project.LMSSU.DTO.IncompleteTaskResponseDTO;
import com.project.LMSSU.Entity.Attending;
import com.project.LMSSU.Entity.Subject;
import com.project.LMSSU.Repository.AttendingRepository;
import com.project.LMSSU.Repository.IncompleteSubjectContentsRepository;
import com.project.LMSSU.Repository.SubjectContentsRepository;
import com.project.LMSSU.Repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class IncompleteTaskService {
    private final AttendingRepository attendingRepository;
    private final SubjectRepository subjectRepository;
    private final IncompleteSubjectContentsRepository incompleteSubjectContentsRepository;
    private final SubjectContentsRepository subjectContentsRepository;
}
