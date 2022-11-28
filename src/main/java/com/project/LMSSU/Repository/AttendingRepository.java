package com.project.LMSSU.Repository;

import com.project.LMSSU.Entity.Attending;
import com.project.LMSSU.Entity.Student;
import com.project.LMSSU.Entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AttendingRepository extends JpaRepository<Attending, Long> {
    List<Attending> findAttendingByStudentId(Long studentId);
    Optional<Attending> findAttendingByStudentIdAndSubjectId(Long studentId, Long subjectId);

    @Query("select at.subject from Attending at where at.student.id = :studentId")
    List<Subject> findSubjectByStudentId(@Param("studentId") Long studentId);

    @Query("select at.subject.id from Attending at where at.student.id = :studentId")
    List<Long> findSubjectIdByStudentId(@Param("studentId") Long studentId);
}
