package com.study.modoos.study.request;

import com.study.modoos.feedback.entity.Attendance;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOneAttendanceRequest {
    private Long id;    //평가받는 회원 아이디

    private Attendance attendance;
}
