package com.study.modoos.study.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAllAttendanceRequest {
    private List<CreateOneAttendanceRequest> attendanceList;
}
