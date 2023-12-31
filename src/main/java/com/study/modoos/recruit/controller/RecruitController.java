package com.study.modoos.recruit.controller;

import com.study.modoos.common.CurrentUser;
import com.study.modoos.common.response.NormalResponse;
import com.study.modoos.member.entity.Member;
import com.study.modoos.recruit.request.ChangeRecruitRequest;
import com.study.modoos.recruit.request.RecruitRequest;
import com.study.modoos.recruit.response.RecruitIdResponse;
import com.study.modoos.recruit.response.RecruitInfoResponse;
import com.study.modoos.recruit.response.RecruitListInfoResponse;
import com.study.modoos.recruit.service.RecruitService;
import com.study.modoos.study.entity.StudyStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recruit")
public class RecruitController {
    private final RecruitService recruitService;


    @PostMapping("/post")
    public ResponseEntity<RecruitIdResponse> createRecruit(@CurrentUser Member member, @RequestBody RecruitRequest recruitRequest) {
        return ResponseEntity.ok(recruitService.postRecruit(member, recruitRequest));
    }

    @GetMapping("/postInfo/{id}")
    public ResponseEntity<RecruitInfoResponse> getRecruit(@CurrentUser Member member, @PathVariable(value = "id") Long id) {
        return ResponseEntity.ok(recruitService.oneRecruit(member, id));
    }

    @GetMapping("/posts")
    public ResponseEntity<Slice<RecruitListInfoResponse>> getRecruitList(@CurrentUser Member member,
                                                                         @RequestParam(value = "category", defaultValue = "") List<String> category,
                                                                         @RequestParam(value = "searchBy", required = false) String search,
                                                                         @RequestParam(value = "lastId", required = false) Long lastId,
                                                                         @RequestParam(value = "sortBy", defaultValue = "createdAt") String sortBy,
                                                                         @PageableDefault(sort = "createdAt", direction = DESC) Pageable pageable) {


        return ResponseEntity.ok(recruitService.getRecruitList(member, search, category, lastId, sortBy, pageable));

    }

    @PutMapping("/{id}")
    public ResponseEntity<RecruitIdResponse> changeRecruit(@CurrentUser Member member, @PathVariable(value = "id") Long id, @RequestBody ChangeRecruitRequest request) {
        return ResponseEntity.ok(recruitService.changeRecruit(member, id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<NormalResponse> deleteRecruit(@CurrentUser Member member, @PathVariable(value = "id") Long id) {
        recruitService.deleteRecruit(member, id);
        return ResponseEntity.ok(NormalResponse.success());
    }

    @GetMapping("/my")
    public ResponseEntity<Slice<RecruitListInfoResponse>> getMyStudyList(@CurrentUser Member member, @RequestParam(required = false) StudyStatus status,
                                                                         @PageableDefault(sort = "createdAt", direction = DESC) Pageable pageable) {
        return ResponseEntity.ok(recruitService.getMyStudyList(member, status, pageable));
    }

    @GetMapping("/my-recruit")
    public ResponseEntity<Slice<RecruitListInfoResponse>> getMyRecruitList(@CurrentUser Member member,
                                                                           @PageableDefault(sort = "createdAt", direction = DESC) Pageable pageable){
        return  ResponseEntity.ok(recruitService.getMyRecruit(member, pageable));
    }
}
