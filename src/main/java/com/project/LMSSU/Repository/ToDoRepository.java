package com.project.LMSSU.Repository;

import com.project.LMSSU.Entity.ToDo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ToDoRepository extends JpaRepository<ToDo, Long> {
    @Query("SELECT t FROM ToDo t WHERE t.student.id = ?1 AND t.subject.id = ?2 AND t.isDone IS NOT NULL")
    List<ToDo> selectJPQLByStudentIdAndSubjectId(Long studentId, Long subjectId);
}
