package com.nimblix.SchoolPEPProject.ServiceImpl;

import com.nimblix.SchoolPEPProject.Model.Attendance;
import com.nimblix.SchoolPEPProject.Repository.AttendanceRepository;
import com.nimblix.SchoolPEPProject.Repository.StudentRepository;
import com.nimblix.SchoolPEPProject.Request.AttendanceRequest;
import com.nimblix.SchoolPEPProject.Service.TeacherService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeacherServiceImpl implements TeacherService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public String markAttendance(AttendanceRequest request, Long teacherId) {

        // Convert Long studentId from request to Integer to match StudentRepository
        Integer studentId = request.getStudentId().intValue();

        // Validate student
        if (!studentRepository.existsById(studentId)) {
            return "Student not found!";
        }

        // Check if attendance is already marked
        boolean exists = attendanceRepository
                .existsByStudentIdAndAttendanceDate(studentId, request.getAttendanceDate());

        if (exists) {
            return "Attendance already marked for this student!";
        }

        // Create attendance record
        Attendance attendance = new Attendance();
        attendance.setStudentId(studentId);
        attendance.setAttendanceDate(request.getAttendanceDate());
        attendance.setAttendanceStatus(request.getAttendanceStatus());
        attendance.setTeacherId(teacherId);

        attendanceRepository.save(attendance);

        return "Attendance marked successfully";
    }
}
