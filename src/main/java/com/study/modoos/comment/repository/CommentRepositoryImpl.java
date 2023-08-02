package com.study.modoos.comment.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.study.modoos.comment.entity.Comment;
import com.study.modoos.comment.response.CommentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.*;

import static com.study.modoos.comment.entity.QComment.comment;
@RequiredArgsConstructor
@Repository
public class CommentRepositoryImpl implements CommentCustomRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<CommentResponse> findByStudyId(Long id) {

        List<Comment> comments = queryFactory.selectFrom(comment)
                .leftJoin(comment.parent).fetchJoin()
                .where(comment.study.id.eq(id))
                .orderBy(comment.parent.id.asc().nullsFirst(),
                        comment.createdAt.asc())
                .fetch();

        List<CommentResponse> commentResponseDTOList = new ArrayList<>();
        Map<Long, CommentResponse> commentDTOHashMap = new HashMap<>();

        comments.forEach(c -> {
            CommentResponse commentResponseDTO = CommentResponse.convertCommentToDto(c);
            commentDTOHashMap.put(commentResponseDTO.getId(), commentResponseDTO);
            if (c.getParent() != null) commentDTOHashMap.get(c.getParent().getId()).getChildren().add(commentResponseDTO);
            else commentResponseDTOList.add(commentResponseDTO);
        });
        return commentResponseDTOList;
    }

    @Override
    public Optional<Comment> findCommentByIdWithParent(Long id) {

        Comment selectedComment = queryFactory.select(comment)
                .from(comment)
                .leftJoin(comment.parent).fetchJoin()
                .where(comment.id.eq(id))
                .fetchOne();

        return Optional.ofNullable(selectedComment);
    }
}