package com.nimblix.SchoolPEPProject.Request;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor  // Generates a no-argument constructor
@AllArgsConstructor // Generates an all-argument constructor
public class AttendanceRequest {
    private Long studentId;
    private String attendanceDate;  // Keep as String as per your requirement
    private String attendanceStatus;
}
