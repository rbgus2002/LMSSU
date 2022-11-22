package com.project.LMSSU.Repository;

import com.project.LMSSU.Entity.Attending;
import com.project.LMSSU.Entity.Subject;
import com.project.LMSSU.Entity.SubjectContents;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubjectContentsRepository extends JpaRepository<SubjectContents, Long> {
    List<SubjectContents> findBySubjectId(Long subjectId);
}
