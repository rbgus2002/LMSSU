package com.project.LMSSU.Repository;

import com.project.LMSSU.Entity.Attending;
import com.project.LMSSU.Entity.Subject;
import com.project.LMSSU.Entity.SubjectContents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubjectContentsRepository extends JpaRepository<SubjectContents, Long> {
    List<SubjectContents> findBySubjectId(Long subjectId);
    List<SubjectContents> findBySubjectIdOrderByWeekAsc(Long subjectId);

    @Query("select sc from SubjectContents sc where sc.subject.id = :subjectId and sc.week = :week")
    List<SubjectContents> findSubjectContentsBySubjectIdAndWeek(@Param("subjectId") Long subjectId, @Param("week") int week);
}
