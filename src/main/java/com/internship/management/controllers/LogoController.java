package com.internship.management.controllers;


import com.internship.management.entities.Enterprise;
import com.internship.management.interfaces.PostOffer;
import com.internship.management.services.MinioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/logos")
@RequiredArgsConstructor
public class LogoController {

    private final MinioService minioService;
    private final PostOffer postOffer;

    @PostMapping("/{enterpriseId}/logo")
    public ResponseEntity<String> uploadEnterpriseLogo(
            @PathVariable Long enterpriseId,
            @RequestParam("file") MultipartFile file
    ) {
        Enterprise enterprise = postOffer.getByEnterpriseId(enterpriseId);

        String fileName = minioService.uploadLogo(file);

        enterprise.setLogoUrl(fileName);
        postOffer.saveUser(enterprise);

        return ResponseEntity.ok("Logo uploaded and linked to enterprise " + enterprise.getName());
    }

    @GetMapping("/{enterpriseId}/logo")
    public ResponseEntity<String> getEnterpriseLogo(@PathVariable Long enterpriseId) {

        Enterprise enterprise = postOffer.getByEnterpriseId(enterpriseId);

        if (enterprise.getLogoUrl() == null) {
            return ResponseEntity.badRequest().body("Enterprise has no logo yet");
        }

        String url = minioService.getFileUrl(enterprise.getLogoUrl());
        return ResponseEntity.ok(url);
    }
}

