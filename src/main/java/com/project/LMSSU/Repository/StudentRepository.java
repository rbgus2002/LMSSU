package com.project.LMSSU.Repository;

import com.project.LMSSU.Entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
