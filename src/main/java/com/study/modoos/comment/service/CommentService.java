package com.study.modoos.comment.service;

import com.study.modoos.alarm.entity.Alarm;
import com.study.modoos.alarm.entity.AlarmType;
import com.study.modoos.alarm.repository.AlarmRepository;
import com.study.modoos.comment.entity.Comment;
import com.study.modoos.comment.mapper.CommentRequestMapper;
import com.study.modoos.comment.repository.CommentRepository;
import com.study.modoos.comment.request.CommentRequest;
import com.study.modoos.comment.response.CommentResponse;
import com.study.modoos.common.exception.ErrorCode;
import com.study.modoos.common.exception.ModoosException;
import com.study.modoos.member.entity.Member;
import com.study.modoos.study.entity.Study;
import com.study.modoos.study.repository.StudyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final StudyRepository studyRepository;
    private final CommentRequestMapper commentRequestMapper;
    private final AlarmRepository alarmRepository;

    public List<CommentResponse> getComment(Long recruitId) {
        Study recruit = studyRepository.findById(recruitId)
                .orElseThrow(() -> new ModoosException(ErrorCode.STUDY_NOT_FOUND));
        return commentRepository.findByStudyId(recruit.getId());
    }

    public void insertComment(Member currentUser, Long recruitId, CommentRequest commentRequest) {
        Study recruit = studyRepository.findById(recruitId)
                .orElseThrow(() -> new ModoosException(ErrorCode.STUDY_NOT_FOUND));
        Comment comment = commentRequestMapper.toEntity(commentRequest);

        Comment parentComment;
        if (commentRequest.getParentId() != null) {
            parentComment = commentRepository.findById(commentRequest.getParentId())
                    .orElseThrow(() -> new ModoosException(ErrorCode.COMMENT_NOT_FOUND));
            if (!parentComment.getStudy().equals(recruit)){
                throw new ModoosException(ErrorCode.INVALID_PARENT_ID);
            }
            comment.updateParent(parentComment);
        }
        comment.updateWriter(currentUser);
        comment.updateStudy(recruit);
        commentRepository.save(comment);

        //답댓글인 경우
        if (commentRequest.getParentId() != null){
            Alarm alarm = new Alarm(comment.getParent().getWriter(), recruit, comment, String.format("%s 이(가) %s 스터디의 회원님의 댓글에 대댓글을 작성하였습니다.",currentUser.getNickname(),recruit.getTitle()), AlarmType.REPLY_OF_MY_COMMENT);
            alarmRepository.save(alarm);
        }
        //댓글인 경우
        else{
            Alarm alarm = new Alarm(recruit.getLeader(), recruit, comment, String.format("%s 이(가) 회원님의 %s 스터디에 댓글을 작성하였습니다.",currentUser.getNickname(), recruit.getTitle()), AlarmType.MY_STUDY_COMMENT);
            alarmRepository.save(alarm);
        }
    }

    public void updateComment(Member currentUser, Long commentId, CommentRequest commentRequest) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ModoosException(ErrorCode.COMMENT_NOT_FOUND));

        if (!currentUser.getId().equals(comment.getWriter().getId())) {
            throw new ModoosException(ErrorCode.INVALID_WRITER);
        }
        comment.updateContent(commentRequest.getContent());
    }

    public void deleteComment(Member currentUser, Long commentId) {
        Comment comment = commentRepository.findCommentByIdWithParent(commentId)
                .orElseThrow(() -> new ModoosException(ErrorCode.COMMENT_NOT_FOUND));
        if (!currentUser.getId().equals(comment.getWriter().getId())) {
            throw new ModoosException(ErrorCode.INVALID_DELETE);
        }
        if (comment.getChildren().size() != 0) {
            comment.changeIsDeleted(true);
        } else {
            commentRepository.delete(getDeletableAncestorComment(comment));
        }
    }

    private Comment getDeletableAncestorComment(Comment comment) {
        Comment parent = comment.getParent();
        if (parent != null && parent.getChildren().size() == 1 && parent.getIsDeleted())
            // 부모가 있고, 부모의 자식이 1개(지금 삭제하는 댓글)이고, 부모의 삭제 상태가 TRUE인 댓글이라면 재귀
            return getDeletableAncestorComment(parent);
        return comment; // 삭제해야하는 댓글 반환
    }
}