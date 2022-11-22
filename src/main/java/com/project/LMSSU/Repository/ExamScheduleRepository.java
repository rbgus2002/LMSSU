package com.project.LMSSU.Repository;

import com.project.LMSSU.Entity.Attending;
import com.project.LMSSU.Entity.ExamSchedule;
import com.project.LMSSU.Entity.Student;
import com.project.LMSSU.Entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ExamScheduleRepository extends JpaRepository<ExamSchedule, Long> {
    List<ExamSchedule> findByStudentId(Long studentId);
    Optional<ExamSchedule> findByStudentIdAndSubjectIdAndDate(Long studentId, Long subjectId, LocalDate date);
}
