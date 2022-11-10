package com.project.LMSSU.Repository;

import com.project.LMSSU.Entity.ToDo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ToDoRepository extends JpaRepository<ToDo, Long> {
}
