package com.internship.management.interfaces;

import com.internship.management.entities.*;


public interface InternshipService {

    Enterprise registerEnterprise(Enterprise enterprise);

    Student registerStudent(Student student);

    Teacher registerTeacher(Teacher teacher);

    Users getUserByEmail(String email);

}
