package com.internship.management.controllers;


import com.internship.management.InternshipMapper;
import com.internship.management.dto.EnterpriseRequestDto;
import com.internship.management.entities.Enterprise;
import com.internship.management.entities.Student;
import com.internship.management.entities.Teacher;
import com.internship.management.services.InternshipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(path ="internshipManagement")
@RequiredArgsConstructor
public class InternshipController {

    private final InternshipService internshipService;
    private final InternshipMapper internshipMapper;

    @PostMapping("/registerEnterprise")
    public ResponseEntity<String> registerEnterprise(@RequestBody EnterpriseRequestDto enterpriseRequestDto) {

        Enterprise toEntityEnterprise = internshipMapper.toEntity(enterpriseRequestDto);
        Enterprise registeredEnterprise = internshipService.registerEnterprise(toEntityEnterprise);
        return ResponseEntity.ok().body(registeredEnterprise.getName() + " company has registered successfully");
    }

    @PostMapping("/registerStudent")
    public ResponseEntity<String> registerEnterprise(@RequestBody Student student) {

        Student registeredStudent = internshipService.registerStudent(student);
        return ResponseEntity.ok().body( registeredStudent.getName() + " student has registered successfully");
    }

    @PostMapping("/registerTeacher")
    public ResponseEntity<String> registerEnterprise(@RequestBody Teacher teacher) {
        Teacher registeredTeacher = internshipService.registerTeacher(teacher);
        return ResponseEntity.ok().body(registeredTeacher.getName() + " teacher has registered successfully");
    }

}
