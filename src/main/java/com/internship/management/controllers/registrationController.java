package com.internship.management.controllers;


import com.internship.management.RegistrationMapper;
import com.internship.management.dto.UserResponseDto;
import com.internship.management.dto.registration.EnterpriseRegistrationRequestDto;
import com.internship.management.dto.registration.StudentRegistrationRequestDto;
import com.internship.management.dto.registration.TeacherRegistrationRequestDto;
import com.internship.management.dto.registration.TokenVerificationRequestDto;
import com.internship.management.entities.Enterprise;
import com.internship.management.entities.Student;
import com.internship.management.entities.Teacher;
import com.internship.management.entities.Users;
import com.internship.management.services.InternshipService;
import com.internship.management.services.registrationService.VerificationTokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(path ="registration")
@RequiredArgsConstructor
public class registrationController {

    private final InternshipService internshipService;
    private final RegistrationMapper internshipMapper;
    private final VerificationTokenService verificationTokenService;

    @PostMapping("/registerEnterprise")
    public ResponseEntity<String> create(@Valid @RequestBody EnterpriseRegistrationRequestDto enterpriseRequestDto) {

        Enterprise toEnterpriseEntity = internshipMapper.toEntity(enterpriseRequestDto);
        Enterprise registeredEnterprise = internshipService.registerEnterprise(toEnterpriseEntity);
        return ResponseEntity.ok().body(registeredEnterprise.getName() + " Company" + " is registered successfully");
    }

    @PostMapping("/registerStudent")
    public ResponseEntity<String> create(@Valid @RequestBody StudentRegistrationRequestDto studentRequestDto) {

        Student toStudentEntity = internshipMapper.toEntity(studentRequestDto);
        Student registeredStudent = internshipService.registerStudent(toStudentEntity);

        return ResponseEntity.ok().body(registeredStudent.getName() + " student" + " is registered successfully");
    }

    @PostMapping("/registerTeacher")
    public ResponseEntity<String> create(@Valid @RequestBody TeacherRegistrationRequestDto teacherRequestDto) {

        Teacher toTeacherEntity = internshipMapper.toEntity(teacherRequestDto);
        Teacher registeredTeacher = internshipService.registerTeacher(toTeacherEntity);

        return ResponseEntity.ok().body(registeredTeacher.getName() + " Company" + " is registered successfully");
    }

    @PostMapping("/verifyEnterpriseEmail")
    public ResponseEntity<UserResponseDto> verifyEnterpriseEmail(@Valid @RequestBody TokenVerificationRequestDto tokenVerificationRequestDto) {

           Users userVerified = verificationTokenService.verifyCode(tokenVerificationRequestDto.getEmail(), tokenVerificationRequestDto.getToken());
           UserResponseDto userResponseDto = internshipMapper.toDto((Enterprise) userVerified);
           return ResponseEntity.ok().body(userResponseDto);
    }


    @PostMapping("/verifyStudentEmail")
    public ResponseEntity<UserResponseDto> verifyStudentEmail(@Valid @RequestBody TokenVerificationRequestDto tokenVerificationRequestDto) {

        Users userVerified = verificationTokenService.verifyCode(tokenVerificationRequestDto.getEmail(), tokenVerificationRequestDto.getToken());
        UserResponseDto userResponseDto = internshipMapper.toDto((Student) userVerified);
        return ResponseEntity.ok().body(userResponseDto);
    }


    @PostMapping("/verifyTeacherEmail")
    public ResponseEntity<UserResponseDto> verifyTeacherEmail(@Valid @RequestBody TokenVerificationRequestDto tokenVerificationRequestDto) {

        Users userVerified = verificationTokenService.verifyCode(tokenVerificationRequestDto.getEmail(), tokenVerificationRequestDto.getToken());
        UserResponseDto userResponseDto = internshipMapper.toDto((Teacher) userVerified);
        return ResponseEntity.ok().body(userResponseDto);
    }

}
