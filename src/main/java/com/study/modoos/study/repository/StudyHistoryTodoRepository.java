package com.study.modoos.study.repository;

import com.study.modoos.study.entity.StudyHistory;
import com.study.modoos.study.entity.StudyHistoryTodo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudyHistoryTodoRepository extends JpaRepository<StudyHistoryTodo, Long> {

    List<StudyHistoryTodo> findStudyHistoryTodoByStudyHistory(StudyHistory studyHistory);
}
