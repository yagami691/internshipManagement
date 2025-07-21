package com.internship.management.interfaces;

import com.internship.management.entities.Enterprise;
import com.internship.management.entities.Student;
import com.internship.management.entities.Teacher;
import org.springframework.security.core.userdetails.UserDetails;


public interface InternshipService {

    Enterprise registerEnterprise(Enterprise enterprise);
    Student registerStudent(Student student);
    Teacher registerTeacher(Teacher teacher);


}
