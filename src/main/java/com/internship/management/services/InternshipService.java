package com.internship.management.services;

import com.internship.management.entities.Enterprise;
import com.internship.management.entities.Student;
import com.internship.management.entities.Teacher;
import com.internship.management.entities.Users;

public interface InternshipService {

    Enterprise registerEnterprise(Enterprise enterprise);
    Student registerStudent(Student student);
    Teacher registerTeacher(Teacher teacher);
}
