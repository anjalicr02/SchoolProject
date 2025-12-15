package com.nimblix.SchoolPEPProject.Service;

import com.nimblix.SchoolPEPProject.Request.AttendanceRequest;

public interface TeacherService {
    String markAttendance(AttendanceRequest request, Long teacherId);
}
