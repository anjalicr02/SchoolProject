package com.nimblix.SchoolPEPProject.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nimblix.SchoolPEPProject.Model.Teacher;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
	
    Teacher findByEmailId(String emailId);
}
