package com.study.modoos.comment.controller;

import com.study.modoos.comment.request.CommentRequest;
import com.study.modoos.comment.response.CommentResponse;
import com.study.modoos.comment.service.CommentService;
import com.study.modoos.common.CurrentUser;
import com.study.modoos.common.response.NormalResponse;
import com.study.modoos.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {
    private final CommentService commentService;

    //댓글, 답댓글 조회
    @GetMapping("/{recruitId}")
    public List<CommentResponse> getComment(@PathVariable(value = "recruitId") Long recruitId){
        return  commentService.getComment(recruitId);
    }
    //댓글, 답댓글 생성
    @PostMapping("/{recruitId}")
    public ResponseEntity<NormalResponse> insertComment(@CurrentUser Member member, @PathVariable(value = "recruitId") Long recruitId, @RequestBody CommentRequest commentRequest){
        commentService.insertComment(member, recruitId, commentRequest);
        return ResponseEntity.ok(NormalResponse.success());
    }
    //댓글, 답댓글 수정
    @PutMapping("/{commentId}")
    public ResponseEntity<NormalResponse> updateComment(@CurrentUser Member member, @PathVariable(value = "commentId") Long commentId, @RequestBody CommentRequest commentRequest) {
        commentService.updateComment(member, commentId, commentRequest);
        return ResponseEntity.ok(NormalResponse.success());
    }
    //댓글, 답댓글 삭제
    //부모 댓글의 경우, "삭제된 댓글입니다"
    //부모 댓글이 isDeleted true 이고, 답댓글(삭제하려는 댓글)이 1개 남아 있으면 부모 댓글과 함께 삭제
    //그 외 삭제 처리
    @DeleteMapping("/{commentId}")
    public ResponseEntity<NormalResponse> deleteComment(@CurrentUser Member member, @PathVariable(value = "commentId") Long commentId) {
        commentService.deleteComment(member, commentId);
        return ResponseEntity.ok(NormalResponse.success());
    }
}
