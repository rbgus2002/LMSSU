package com.project.LMSSU.Repository;

import com.project.LMSSU.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
