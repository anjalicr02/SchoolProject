package com.nimblix.SchoolPEPProject.Controller;

import com.nimblix.SchoolPEPProject.Constants.SchoolConstants;
import com.nimblix.SchoolPEPProject.Model.Teacher;
import com.nimblix.SchoolPEPProject.Repository.TeacherRepository;
import com.nimblix.SchoolPEPProject.Request.AttendanceRequest;
import com.nimblix.SchoolPEPProject.Service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/teacher/attendance")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @PostMapping("/mark")
    public ResponseEntity<?> markAttendance(@RequestHeader("Authorization") String authHeader,
            @RequestBody AttendanceRequest request) {

        return teacherService.markAttendance(authHeader, request);
        
        
    }
}

