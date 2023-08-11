package com.study.modoos.feedback.repository;

import com.study.modoos.feedback.entity.Feedback;
import com.study.modoos.feedback.entity.FeedbackTodo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackTodoRepository extends JpaRepository<FeedbackTodo, Long> {

    List<FeedbackTodo> findFeedbackTodoByFeedback(Feedback feedback);
}
