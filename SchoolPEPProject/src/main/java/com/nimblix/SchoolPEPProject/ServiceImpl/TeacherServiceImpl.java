package com.nimblix.SchoolPEPProject.ServiceImpl;

import com.nimblix.SchoolPEPProject.Constants.SchoolConstants;
import com.nimblix.SchoolPEPProject.Model.Attendance;
import com.nimblix.SchoolPEPProject.Repository.AttendanceRepository;
import com.nimblix.SchoolPEPProject.Repository.StudentRepository;
import com.nimblix.SchoolPEPProject.Repository.TeacherRepository;
import com.nimblix.SchoolPEPProject.Request.AttendanceRequest;
import com.nimblix.SchoolPEPProject.Service.TeacherService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.nimblix.SchoolPEPProject.Model.Teacher;

@Service
public class TeacherServiceImpl implements TeacherService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Override
    public ResponseEntity<?> markAttendance(String authHeader, AttendanceRequest request) {

        Map<String, Object> response = new HashMap<>();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            response.put(SchoolConstants.STATUS, SchoolConstants.STATUS_FAILURE);
            response.put(SchoolConstants.MESSAGE, "Invalid Token!");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        // Role validation
        boolean isTeacher = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals(SchoolConstants.ROLE_TEACHER));

        if (!isTeacher) {
            response.put(SchoolConstants.STATUS, SchoolConstants.STATUS_FAILURE);
            response.put(SchoolConstants.MESSAGE, "Access Denied! Only teachers can mark attendance.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        String emailId = authentication.getName();

        Teacher teacher = teacherRepository.findByEmailId(emailId);
        if (teacher == null) {
            response.put(SchoolConstants.STATUS, SchoolConstants.STATUS_FAILURE);
            response.put(SchoolConstants.MESSAGE, "Teacher not found!");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        Integer studentId = request.getStudentId().intValue();

        if (!studentRepository.existsById(studentId)) {
            response.put(SchoolConstants.STATUS, SchoolConstants.STATUS_FAILURE);
            response.put(SchoolConstants.MESSAGE, "Student not found!");
            return ResponseEntity.badRequest().body(response);
        }

        boolean exists = attendanceRepository
                .existsByStudentIdAndAttendanceDate(studentId, request.getAttendanceDate());

        if (exists) {
            response.put(SchoolConstants.STATUS, SchoolConstants.STATUS_FAILURE);
            response.put(SchoolConstants.MESSAGE, "Attendance already marked!");
            return ResponseEntity.badRequest().body(response);
        }

        Attendance attendance = new Attendance();
        attendance.setStudentId(studentId);
        attendance.setAttendanceDate(request.getAttendanceDate());
        attendance.setAttendanceStatus(request.getAttendanceStatus());
        attendance.setTeacherId(teacher.getId());

        attendanceRepository.save(attendance);

        response.put(SchoolConstants.STATUS, SchoolConstants.STATUS_SUCCESS);
        response.put(SchoolConstants.MESSAGE, "Attendance marked successfully");

        
        return ResponseEntity.ok(response);
    }
}
