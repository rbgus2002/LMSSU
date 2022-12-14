package com.project.LMSSU.Repository;

import com.project.LMSSU.Entity.SubjectContents;
import com.project.LMSSU.Entity.SubjectNotice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubjectNoticeRepository extends JpaRepository<SubjectNotice, Long> {
    List<SubjectNotice> findBySubjectId(Long subjectId);
    List<SubjectNotice> findBySubjectIdOrderByLocalDateDesc(Long subjectId);
}
