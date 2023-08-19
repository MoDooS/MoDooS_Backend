package com.study.modoos.alarm.response;

import com.study.modoos.alarm.entity.Alarm;
import com.study.modoos.alarm.entity.AlarmType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AlarmResponse {
    private Long alarmId;
    private String receiverName;
    private Long receiverId;
    private String studyName;
    private Long studyId;
    private String content;
    private Long commentId;
    private AlarmType alarmType;
    private boolean isRead;

    public static AlarmResponse of(Alarm alarm) {
        AlarmResponse response = new AlarmResponse();
        response.alarmId = alarm.getId();
        response.receiverName = alarm.getMember().getNickname();
        response.receiverId = alarm.getMember().getId();
        response.studyName = alarm.getStudy().getTitle();
        response.studyId = alarm.getStudy().getId();
        response.content = alarm.getContent();
        response.commentId = alarm.getComment().getId();
        response.alarmType = alarm.getAlarmType();
        response.isRead = alarm.isRead();
        return response;
    }
}
