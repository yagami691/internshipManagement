package com.internship.management.controllers;


import com.internship.management.dto.StudentResponseDto;
import com.internship.management.dto.TeacherResponseDto;
import com.internship.management.dto.postOffer.EnterpriseResponseDto;
import com.internship.management.entities.Enterprise;
import com.internship.management.entities.Student;
import com.internship.management.entities.Teacher;
import com.internship.management.entities.Users;
import com.internship.management.interfaces.ChartInterface;
import com.internship.management.interfaces.PostOffer;
import com.internship.management.mappers.PostOfferMapper;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @PutMapping("/Enterprise/{id}/approve")
    public ResponseEntity<EnterpriseResponseDto> approveEnterprise(@PathVariable Long id, @RequestParam boolean approved){

        Enterprise enterprise = postOffer.getByEnterpriseId(id);
        enterprise.setInPartnership(approved);
        postOffer.saveUser(enterprise);

        return ResponseEntity.ok(postOfferMapper.toDtoEnterprise(enterprise));
    }


    @DeleteMapping("/deleteAdminAccount")
    public ResponseEntity<String> delete(){

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Users user = postOffer.getUserByEmail(email);

        postOffer.deleteUser(user.getId());

        return ResponseEntity.ok("admin deleted successfully");
    }
}