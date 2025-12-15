package com.nimblix.SchoolPEPProject.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nimblix.SchoolPEPProject.Model.Attendance;

public interface AttendanceRepository extends JpaRepository<Attendance,Long>{
	boolean existsByStudentIdAndAttendanceDate(Integer studentId, String attendanceDate);

}
