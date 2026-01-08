package com.nimblix.SchoolPEPProject.Service;

import org.springframework.http.ResponseEntity;

import com.nimblix.SchoolPEPProject.Request.AttendanceRequest;

public interface TeacherService {
	 ResponseEntity<?> markAttendance(String authHeader, AttendanceRequest request);
}
