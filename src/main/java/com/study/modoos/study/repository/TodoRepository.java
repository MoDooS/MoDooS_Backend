package com.study.modoos.study.repository;

import com.study.modoos.study.entity.Study;
import com.study.modoos.study.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {

    List<Todo> findTodoByStudy(Study study);
}
