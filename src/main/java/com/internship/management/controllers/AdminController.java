package com.internship.management.controllers;


import com.internship.management.entities.Users;
import com.internship.management.interfaces.ChartInterface;
import com.internship.management.interfaces.PostOffer;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;

@RestController
@RequestMapping(path = "/api/admin")
@RequiredArgsConstructor
@SecurityRequirement(name = "JWT")
public class AdminController {

    private final ChartInterface chartInterface;
    private final PostOffer postOffer;

    @GetMapping("/internships.xlsx")
    public ResponseEntity<byte[]> downloadInternshipsExcel() throws IOException {
        ByteArrayInputStream stream = chartInterface.exportInternshipsByDepartment();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=internshipBySector" + LocalDate.now() + ".xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(stream.readAllBytes());
    }

    @DeleteMapping("/deleteAdminAccount")
    public ResponseEntity<String> delete(){

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Users user = postOffer.getUserByEmail(email);

        postOffer.deleteUser(user.getId());

        return ResponseEntity.ok("admin deleted successfully");
    }
}
