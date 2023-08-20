package com.study.modoos.alarm.response;

import com.study.modoos.alarm.entity.Alarm;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReadAlarmResponse {
    Long alarmId;
    boolean isRead;

    public static ReadAlarmResponse readAlarm(Alarm alarm, boolean isRead) {
        return ReadAlarmResponse.builder()
                .alarmId(alarm.getId())
                .isRead(isRead)
                .build();
    }
}
