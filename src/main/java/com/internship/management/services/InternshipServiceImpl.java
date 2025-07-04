package com.internship.management.services;



import com.internship.management.entities.Enterprise;
import com.internship.management.entities.Student;
import com.internship.management.entities.Teacher;
import com.internship.management.repositories.EnterpriseRepository;
import com.internship.management.repositories.StudentRepository;
import com.internship.management.repositories.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InternshipServiceImpl implements InternshipService {


    private final EnterpriseRepository enterpriseRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;

   public Enterprise registerEnterprise(Enterprise enterprise) {
        return enterpriseRepository.save(enterprise);
   }

    public Student registerStudent(Student student) {
        return studentRepository.save(student);
    }

    public Teacher registerTeacher(Teacher teacher) {
        return teacherRepository.save(teacher);
    }

}
