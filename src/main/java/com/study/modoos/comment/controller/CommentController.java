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

    @GetMapping("/{recruitId}")
    public List<CommentResponse> getComment(@PathVariable(value = "recruitId") Long recruitId){
        return  commentService.getComment(recruitId);
    }

    @PostMapping("/{recruitId}")
    public ResponseEntity<NormalResponse> insertComment(@CurrentUser Member member, @PathVariable(value = "recruitId") Long recruitId, @RequestBody CommentRequest commentRequest){
        commentService.insertComment(member, recruitId, commentRequest);
        return ResponseEntity.ok(NormalResponse.success());
    }
    @PutMapping("/{commentId}")
    public ResponseEntity<NormalResponse> updateComment(@CurrentUser Member member, @PathVariable(value = "commentId") Long commentId, @RequestBody CommentRequest commentRequest) {
        commentService.updateComment(member, commentId, commentRequest);
        return ResponseEntity.ok(NormalResponse.success());
    }
    @DeleteMapping("/{commentId}")
    public ResponseEntity<NormalResponse> deleteComment(@CurrentUser Member member, @PathVariable(value = "commentId") Long commentId) {
        commentService.deleteComment(member, commentId);
        return ResponseEntity.ok(NormalResponse.success());
    }
}
