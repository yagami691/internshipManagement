package com.internship.management.controllers;


import com.internship.management.mappers.RegistrationMapper;
import com.internship.management.dto.UserResponseDto;
import com.internship.management.dto.registration.EnterpriseRegistrationRequestDto;
import com.internship.management.dto.registration.StudentRegistrationRequestDto;
import com.internship.management.dto.registration.TeacherRegistrationRequestDto;
import com.internship.management.dto.registration.TokenVerificationRequestDto;
import com.internship.management.entities.Enterprise;
import com.internship.management.entities.Student;
import com.internship.management.entities.Teacher;
import com.internship.management.entities.Users;
import com.internship.management.interfaces.InternshipService;
import com.internship.management.services.registrationService.VerificationTokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(path ="registration")
@RequiredArgsConstructor
public class registrationController {

    private final InternshipService internshipService;
    private final RegistrationMapper registrationMapper;
    private final VerificationTokenService verificationTokenService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/registerEnterprise")
    public ResponseEntity<String> create(@Valid @RequestBody EnterpriseRegistrationRequestDto enterpriseRequestDto) {

        Enterprise toEnterpriseEntity = registrationMapper.toEntity(enterpriseRequestDto,passwordEncoder);
        Enterprise registeredEnterprise = internshipService.registerEnterprise(toEnterpriseEntity);

        return ResponseEntity.ok().body(registeredEnterprise.getName() + " Company" + " is registered successfully");
    }

    @PostMapping("/registerStudent")
    public ResponseEntity<String> create(@Valid @RequestBody StudentRegistrationRequestDto studentRequestDto) {

        Student toStudentEntity = registrationMapper.toEntity(studentRequestDto, passwordEncoder);
        Student registeredStudent = internshipService.registerStudent(toStudentEntity);

        return ResponseEntity.ok().body(registeredStudent.getName() + " student" + " is registered successfully");
    }

    @PostMapping("/registerTeacher")
    public ResponseEntity<String> create(@Valid @RequestBody TeacherRegistrationRequestDto teacherRequestDto) {

        Teacher toTeacherEntity = registrationMapper.toEntity(teacherRequestDto, passwordEncoder);
        Teacher registeredTeacher = internshipService.registerTeacher(toTeacherEntity);

        return ResponseEntity.ok().body(registeredTeacher.getName() + " teacher" + " is registered successfully");
    }

    @PostMapping("/verifyEnterpriseEmail")
    public ResponseEntity<UserResponseDto> verifyEnterpriseEmail(@Valid @RequestBody TokenVerificationRequestDto tokenVerificationRequestDto) {

           Users userVerified = verificationTokenService.verifyCode(tokenVerificationRequestDto.getEmail(), tokenVerificationRequestDto.getToken());
           UserResponseDto userResponseDto = registrationMapper.toDto((Enterprise) userVerified);

           return ResponseEntity.ok().body(userResponseDto);
    }


    @PostMapping("/verifyStudentEmail")
    public ResponseEntity<UserResponseDto> verifyStudentEmail(@Valid @RequestBody TokenVerificationRequestDto tokenVerificationRequestDto) {

        Users userVerified = verificationTokenService.verifyCode(tokenVerificationRequestDto.getEmail(), tokenVerificationRequestDto.getToken());
        UserResponseDto userResponseDto = registrationMapper.toDto((Student) userVerified);

        return ResponseEntity.ok().body(userResponseDto);
    }


    @PostMapping("/verifyTeacherEmail")
    public ResponseEntity<UserResponseDto> verifyTeacherEmail(@Valid @RequestBody TokenVerificationRequestDto tokenVerificationRequestDto) {

        Users userVerified = verificationTokenService.verifyCode(tokenVerificationRequestDto.getEmail(), tokenVerificationRequestDto.getToken());
        UserResponseDto userResponseDto = registrationMapper.toDto((Teacher) userVerified);

        return ResponseEntity.ok().body(userResponseDto);
    }

    @PostMapping("/resendToken")
    public ResponseEntity<String> resendToken(@RequestParam String email) {
        Users user = internshipService.getUserByEmail(email);

        if (user.isEmailVerified()) {
            return ResponseEntity.badRequest().body("User is already verified");
        }

        verificationTokenService.resendToken(user);
        return ResponseEntity.ok("A new token has been sent to your email");
    }


}
