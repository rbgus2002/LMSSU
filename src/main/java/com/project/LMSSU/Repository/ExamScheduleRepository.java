package com.project.LMSSU.Repository;

import com.project.LMSSU.domain.ExamSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExamScheduleRepository extends JpaRepository<ExamSchedule, Long> {
}
