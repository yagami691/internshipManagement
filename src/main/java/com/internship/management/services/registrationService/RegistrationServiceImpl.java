package com.internship.management.services.registrationService;



import com.internship.management.entities.*;
import com.internship.management.repositories.*;
import com.internship.management.interfaces.InternshipService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements InternshipService {

    private final EnterpriseRepository enterpriseRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final VerificationTokenService verificationTokenService;
    private final UsersRepository userRepository;
    private final LogoRepository logoRepository;


    @Transactional
   public Enterprise registerEnterprise(Enterprise enterprise) {

       boolean isEnterpriseEmailExists = userRepository.existsByEmail(enterprise.getEmail());
       if (isEnterpriseEmailExists) {
           throw new IllegalStateException("user with email " + enterprise.getEmail() + " already exists");
       }

       Enterprise newEnterprise = enterpriseRepository.save(enterprise);
       verificationTokenService.createAndSendToken(newEnterprise);

       return newEnterprise;
   }

    public Student registerStudent(Student student) {

        boolean isStudentEmailExists = userRepository.existsByEmail(student.getEmail());
        if (isStudentEmailExists) {
            throw new IllegalStateException("user with email " + student.getEmail() + " already exists");
        }

        Student newStudent = studentRepository.save(student);
        verificationTokenService.createAndSendToken(newStudent);

       return studentRepository.save(student);
    }

    public Teacher registerTeacher(Teacher teacher) {

        boolean isTeacherEmailExists = userRepository.existsByEmail(teacher.getEmail());
        if (isTeacherEmailExists) {
            throw new IllegalStateException("user with email " + teacher.getEmail() + " already exists");
        }

        Teacher newTeacher = teacherRepository.save(teacher);
        verificationTokenService.createAndSendToken(newTeacher);

       return teacherRepository.save(teacher);
    }

    public Users getUserByEmail(String email) {
       return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void saveLogo(Logo logo){
        logoRepository.save(logo);
    }
}
