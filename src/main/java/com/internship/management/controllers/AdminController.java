package com.internship.management.controllers;


import com.internship.management.interfaces.ChartInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;

@RestController
@RequestMapping(path = "/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final ChartInterface chartInterface;

    @GetMapping("/internships.xlsx")
    public ResponseEntity<byte[]> downloadInternshipsExcel() throws IOException {
        ByteArrayInputStream stream = chartInterface.exportInternshipsByDepartment();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=internshipBySector" + LocalDate.now() + ".xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(stream.readAllBytes());
    }
}
