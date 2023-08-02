package com.study.modoos.comment.response;

import com.study.modoos.comment.entity.Comment;
import com.study.modoos.member.entity.Member;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
public class CommentResponse {
    private Long id;
    private Member writer;
    private String content;
    private String createDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
    private String modifiedDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
    private Long studyId;
    private List<CommentResponse> children = new ArrayList<>();

    public CommentResponse(Long id, Member writer, String content, Long studyId){
        this.id = id;
        this.writer = writer;
        this.content = content;
        this.studyId = studyId;
    }
    public static CommentResponse convertCommentToDto(Comment comment){
        return comment.getIsDeleted() ?
                new CommentResponse(comment.getId(), null,"삭제된 댓글입니다", comment.getStudy().getId()) :
                new CommentResponse(comment.getId(), new Member(comment.getWriter()), comment.getContent(),comment.getStudy().getId());
    }
}
