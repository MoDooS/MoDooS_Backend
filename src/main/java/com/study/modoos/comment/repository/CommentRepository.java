package com.study.modoos.comment.repository;

import com.study.modoos.comment.entity.Comment;
import com.study.modoos.study.entity.Study;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentCustomRepository {
}
