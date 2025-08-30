package com.internship.management.controllers;


import com.internship.management.dto.StudentResponseDto;
import com.internship.management.dto.TeacherResponseDto;
import com.internship.management.dto.postOffer.EnterpriseResponseDto;
import com.internship.management.entities.Enterprise;
import com.internship.management.entities.Student;
import com.internship.management.entities.Teacher;
import com.internship.management.enums.EnterpriseState;
import com.internship.management.interfaces.ChartInterface;
import com.internship.management.interfaces.NotificationInterface;
import com.internship.management.interfaces.PostOffer;
import com.internship.management.mappers.PostOfferMapper;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(path = "/api/admin")
@RequiredArgsConstructor
@SecurityRequirement(name = "JWT")
public class AdminController {

    private final ChartInterface chartInterface;
    private final PostOffer postOffer;
    private final PostOfferMapper postOfferMapper;
    private final NotificationInterface notificationInterface;

    @GetMapping("/internships.xlsx")
    public ResponseEntity<byte[]> downloadInternshipsExcel() throws IOException {

        ByteArrayInputStream stream = chartInterface.exportInternshipsByDepartment();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=internshipBySector" + LocalDate.now() + ".xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(stream.readAllBytes());
    }

    @GetMapping("/approvalPendingEnterprise")
    public List<EnterpriseResponseDto> getPendingValidationEnterprise(){

        List<Enterprise> listOfEnterprise = postOffer.getEnterpriseByPartnershipFalse();
        return postOfferMapper.toDtoEnterpriseList(listOfEnterprise);
    }

    @GetMapping("/enterpriseInPartnership")
    public List<EnterpriseResponseDto> getEnterpriseInPartnership(){

        List<Enterprise> listOfEnterpriseInPartnership = postOffer.getEnterpriseByPartnershipTrue();
        return postOfferMapper.toDtoEnterpriseList(listOfEnterpriseInPartnership);
    }

    @GetMapping("/allTeachers")
    public ResponseEntity<List<TeacherResponseDto>> getAllTeachers(){

        List<Teacher> teachers = postOffer.getAllTeachers();
        return ResponseEntity.ok(postOfferMapper.toDtoTeacherList(teachers));
    }

    @GetMapping("/allStudent")
    public ResponseEntity<List<StudentResponseDto>> getAllStudents(){

        List<Student> students = postOffer.getAllStudent();
        return ResponseEntity.ok(postOfferMapper.toDtoStudentList(students));
    }

    @GetMapping("/teacherPagination")
    public Page<TeacherResponseDto> getTeacherPagination(Pageable pageable){

        Page<Teacher> saveTeacher = postOffer.getAllTeacherByPagination(pageable);
        List<TeacherResponseDto>  teachers =  postOfferMapper.toDtoTeacherList(saveTeacher.getContent());

        return new PageImpl<>(teachers, pageable, teachers.size());
    }

    @GetMapping("/studentPagination")
    public Page<StudentResponseDto> getStudentPagination(Pageable pageable){

        Page<Student> saveStudent = postOffer.getAllStudentByPagination(pageable);
        List<StudentResponseDto>  students =  postOfferMapper.toDtoStudentList(saveStudent.getContent());

        return new PageImpl<>(students, pageable, students.size());
    }

    @PutMapping("/Enterprise/{id}/approve")
    public ResponseEntity<EnterpriseResponseDto> approveEnterprise(@PathVariable Long id, @RequestParam boolean approved){

        Enterprise enterprise = postOffer.getByEnterpriseId(id);
        String enterpriseMsg = approved? "Your enterprise was  approved on our internship management platform":
                "Your enterprise was rejected on our internship management platform";

        if(approved){

            enterprise.setEnterpriseState(EnterpriseState.APPROVED);
            enterprise.setInPartnership(true);
            postOffer.saveUser(enterprise);
            notificationInterface.sendNotification(enterprise, enterpriseMsg);

        }else{

            enterprise.setEnterpriseState(EnterpriseState.REJECTED);
            notificationInterface.sendNotification(enterprise, enterpriseMsg);
        }

        return ResponseEntity.ok(postOfferMapper.toDtoEnterprise(enterprise));
    }
}