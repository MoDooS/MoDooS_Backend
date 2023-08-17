package com.study.modoos.ranking.controller;

import com.study.modoos.common.CurrentUser;
import com.study.modoos.member.entity.Member;
import com.study.modoos.ranking.response.RankingResponse;
import com.study.modoos.ranking.service.RankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ranking")
public class RankingController {

    private final RankingService rankingService;

    @GetMapping
    public ResponseEntity<Slice<RankingResponse>> getRanking(@CurrentUser Member currentUser,
                                                             @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(rankingService.getRanking(currentUser, pageable));
    }
}
