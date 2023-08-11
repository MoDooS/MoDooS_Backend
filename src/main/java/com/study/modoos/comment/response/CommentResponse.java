package com.study.modoos.comment.response;

import com.study.modoos.comment.entity.Comment;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentResponse {
    private Long id;
    private Long writerId;
    private String writerNickname;
    private String content;
    private String createDate;
    private String modifiedDate;
    private Long studyId;
    private List<CommentResponse> children = new ArrayList<>();

    public CommentResponse(Long id, Long writerId, String writerNickname, String content, Long studyId, LocalDateTime createDate, LocalDateTime modifiedDate){
        this.id = id;
        this.writerId = writerId;
        this.writerNickname = writerNickname;
        this.content = content;
        this.studyId = studyId;
        this.createDate = createDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
        this.modifiedDate = modifiedDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
    }
    public static CommentResponse convertCommentToDto(Comment comment){
        return comment.getIsDeleted() ?
                new CommentResponse(comment.getId(), null, null,"삭제된 댓글입니다", comment.getStudy().getId(), comment.getCreatedAt(), comment.getUpdatedAt()) :
                new CommentResponse(comment.getId(), comment.getWriter().getId(), comment.getWriter().getNickname(), comment.getContent(),comment.getStudy().getId(), comment.getCreatedAt(), comment.getUpdatedAt());
    }
}
