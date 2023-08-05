package com.study.modoos.heart.contorller;

import com.study.modoos.common.CurrentUser;
import com.study.modoos.heart.response.HeartClickResponse;
import com.study.modoos.heart.response.HeartResponse;
import com.study.modoos.heart.service.HeartService;
import com.study.modoos.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/heart")
public class HeartController {
    private final HeartService heartService;


    @GetMapping("/all")
    public ResponseEntity<Slice<HeartResponse>> getAllHeart(@CurrentUser Member member,
                                                            @PageableDefault(sort = "createdAt", direction = DESC) Pageable pageable){
        return ResponseEntity.ok(heartService.getAllHeart(member, pageable));
    }

    @GetMapping("/{recruitId}")
    public ResponseEntity<HeartClickResponse> clickHeart (@CurrentUser Member member, @PathVariable Long recruitId){
        return ResponseEntity.ok(heartService.clickHeart(member, recruitId));
    }
}
