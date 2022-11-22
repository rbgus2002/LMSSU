package com.project.LMSSU.Repository;

import com.project.LMSSU.Entity.Attending;
import com.project.LMSSU.Entity.Student;
import com.project.LMSSU.Entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AttendingRepository extends JpaRepository<Attending, Long> {
    List<Attending> findAttendingByStudentId(Student student);
}
