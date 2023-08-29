package com.study.modoos.alarm.controller;

import com.study.modoos.alarm.response.AlarmResponse;
import com.study.modoos.alarm.response.ReadAlarmResponse;
import com.study.modoos.alarm.service.AlarmService;
import com.study.modoos.common.CurrentUser;
import com.study.modoos.common.exception.ErrorCode;
import com.study.modoos.common.exception.ModoosException;
import com.study.modoos.common.response.NormalResponse;
import com.study.modoos.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/read/{alarmId}")
    public ResponseEntity<ReadAlarmResponse> readAlarm(@CurrentUser Member currentUser, @PathVariable Long alarmId) {
        return ResponseEntity.ok(alarmService.readAlarm(currentUser, alarmId));
    }

    @GetMapping("/read/all")
    public ResponseEntity<NormalResponse> readAllAlarm(@CurrentUser Member currentUser){
        try {
            alarmService.readAllAlarm(currentUser);
            return ResponseEntity.ok(NormalResponse.success());
        }
        catch (ModoosException ex){
            throw new ModoosException(ErrorCode.MEMBER_HAS_NOT_ALARM);
        }
    }
}

