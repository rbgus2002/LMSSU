package com.project.LMSSU.Repository;

import com.project.LMSSU.Entity.Major;
import com.project.LMSSU.Entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MajorRepository extends JpaRepository<Major, String> {
    Optional<Major> findByMajorName(String majorName);
}
