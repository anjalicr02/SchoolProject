package com.nimblix.SchoolPEPProject.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nimblix.SchoolPEPProject.Model.Teacher;

public interface TeacherRepository extends JpaRepository<Teacher,Long>{
	Teacher findByEmailId(String emailId);
}
