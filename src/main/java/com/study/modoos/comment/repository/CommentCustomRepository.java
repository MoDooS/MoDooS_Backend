package com.study.modoos.comment.repository;

import com.study.modoos.comment.entity.Comment;
import com.study.modoos.comment.response.CommentResponse;

import java.util.List;
import java.util.Optional;

public interface CommentCustomRepository {
    List<CommentResponse> findByStudyId(Long id);
    Optional<Comment> findCommentByIdWithParent(Long id);
}
