package com.study.modoos.alarm.controller;

import com.study.modoos.alarm.response.AlarmResponse;
import com.study.modoos.alarm.service.AlarmService;
import com.study.modoos.common.CurrentUser;
import com.study.modoos.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.data.domain.Sort.Direction.DESC;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/alarm")
public class AlarmController {

    private final AlarmService alarmService;
    @GetMapping("/all")
    public ResponseEntity<Slice<AlarmResponse>> getComment(@CurrentUser Member currentUser,
                                                           @PageableDefault(sort = "createdAt", direction = DESC) Pageable pageable){
        return  ResponseEntity.ok(alarmService.getAlarm(currentUser, pageable));
    }
}
