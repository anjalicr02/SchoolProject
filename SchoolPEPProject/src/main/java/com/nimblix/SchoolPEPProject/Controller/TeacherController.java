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

    @Autowired
    private TeacherRepository teacherRepository;

    @PostMapping("/mark")
    public ResponseEntity<?> markAttendance(@RequestHeader("Authorization") String authHeader,
                                            @RequestBody AttendanceRequest request) {

        Map<String, Object> response = new HashMap<>();

        try {
            // Extract JWT token
            String token = authHeader.substring(7);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null) {
                response.put(SchoolConstants.STATUS, SchoolConstants.STATUS_FAILURE);
                response.put(SchoolConstants.MESSAGE, "Invalid Token!");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            // Validate Teacher Role
            boolean isTeacher = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .anyMatch(role -> role.equals(SchoolConstants.ROLE_TEACHER));

            if (!isTeacher) {
                response.put(SchoolConstants.STATUS, SchoolConstants.STATUS_FAILURE);
                response.put(SchoolConstants.MESSAGE, "Access Denied! Only teachers can mark attendance.");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }

            // Teacher Email from JWT (Username stored in Security Context)
            String emailId = authentication.getName();

            Teacher teacher = teacherRepository.findByEmailId(emailId);
            if (teacher == null) {
                response.put(SchoolConstants.STATUS, SchoolConstants.STATUS_FAILURE);
                response.put(SchoolConstants.MESSAGE, "Teacher not found!");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            Long teacherId = teacher.getId();

            // Call Attendance Service
            String message = teacherService.markAttendance(request, teacherId);

            response.put(SchoolConstants.STATUS, SchoolConstants.STATUS_SUCCESS);
            response.put(SchoolConstants.MESSAGE, message);

            return ResponseEntity.ok(response);

        } catch (Exception ex) {
            response.put(SchoolConstants.STATUS, SchoolConstants.STATUS_FAILURE);
            response.put(SchoolConstants.MESSAGE, "Error: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
