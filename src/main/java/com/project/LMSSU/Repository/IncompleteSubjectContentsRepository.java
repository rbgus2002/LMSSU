package com.project.LMSSU.Repository;

import com.project.LMSSU.Entity.Attending;
import com.project.LMSSU.Entity.IncompleteSubjectContents;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IncompleteSubjectContentsRepository extends JpaRepository<IncompleteSubjectContents, Long> {
    List<IncompleteSubjectContents> findByStudentId(Long studentId);
}
