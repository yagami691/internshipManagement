package com.internship.management.controllers;


import com.internship.management.dto.application.ApplicationResponseDto;
import com.internship.management.dto.application.ApplicationValidationRequestDto;
import com.internship.management.dto.application.NotificationDto;
import com.internship.management.dto.postOffer.OfferRequestDto;
import com.internship.management.dto.postOffer.OfferResponseDto;
import com.internship.management.entities.*;
import com.internship.management.enums.ApplicationState;
import com.internship.management.interfaces.NotificationInterface;
import com.internship.management.interfaces.PostOffer;
import com.internship.management.mappers.PostOfferMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "api/enterprise")
@RequiredArgsConstructor
public class EnterpriseController {

    private final PostOffer postOffer;
    private final PostOfferMapper postOfferMapper;
    private final NotificationInterface notificationInterface;

    @PostMapping("/createOffer")
    public ResponseEntity<OfferResponseDto> create (@ModelAttribute OfferRequestDto offerRequestDto) throws IOException {

           String email = SecurityContextHolder.getContext().getAuthentication().getName();
           Enterprise enterprise = postOffer.getByEnterpriseEmail(email);

           Offer offer = postOfferMapper.toEntity(offerRequestDto);
           offer.setEnterprise(enterprise);

           Convention c = new Convention();
           c.setPdfConvention(offerRequestDto.getPdfConvention().getBytes());
           c.setOffer(offer);

           offer.setConvention(c);
           Offer offerCreated = postOffer.saveOffer(offer);
           OfferResponseDto offerResponseDto = postOfferMapper.toDto(offerCreated);

           return ResponseEntity.ok(offerResponseDto);
    }

    @GetMapping("/Applications")
    public List<ApplicationResponseDto> getApplications() {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Enterprise enterprise = postOffer.getByEnterpriseEmail(email);
        List<Application> applications = postOffer.getAllApplicationsByEnterpriseId(enterprise.getId());

        return postOfferMapper.toDtoApplicationList(applications);

    }

    @GetMapping("/enterpriseNotifications")
    public ResponseEntity<List<NotificationDto>> getUnseenNotifications() {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Enterprise enterprise = postOffer.getByEnterpriseEmail(email);

        List<Notification> unseen = notificationInterface.getAllUnSeenNotificationsByUser(enterprise);

        return ResponseEntity.ok(
                unseen.stream()
                        .map(n -> new NotificationDto(n.getId(), n.getMessage(), n.getCreatedAt()))
                        .toList()
        );
    }

    @GetMapping("/listOfOffers")
    public List<OfferResponseDto> getOffers() {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Enterprise enterprise = postOffer.getByEnterpriseEmail(email);
        List<Offer> offersByEnterpriseId = postOffer.getOfferByEnterpriseId(enterprise.getId());

        return postOfferMapper.toDtoList(offersByEnterpriseId);
    }

    @GetMapping("/enterprise/logo")
    public ResponseEntity<byte[]> getEnterpriseLogo() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Enterprise enterprise = postOffer.getByEnterpriseEmail(email);

        Logo logo = postOffer.getLogoByEnterprise(enterprise);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(logo.getContentType()));

        return new ResponseEntity<>(logo.getLogo(), headers, HttpStatus.OK);
    }



    @GetMapping("/cv/{id}/download")
    public ResponseEntity<byte[]> downloadCV(@PathVariable Long id) {

        Application application = postOffer.getApplicationById(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=student_CV.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(application.getCv());
    }

    @GetMapping("/coverLetter/{id}/download")
    public ResponseEntity<byte[]> downloadCoverLetter(@PathVariable Long id) {

        Application application = postOffer.getApplicationById(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=student_coverLetter.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(application.getCoverLetter());
    }

    @PutMapping("application/{id}/validate")
    public ResponseEntity<String> validateApplication(@PathVariable Long id,
                                                      @RequestParam ApplicationValidationRequestDto applicationValidationRequestDto) {
        Application application = postOffer.getApplicationById(id);

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Enterprise enterprise = postOffer.getByEnterpriseEmail(email);


        String msg =  "Your application is " + application.getState() + " and has been reviewed by the " + enterprise.getName();

        if(applicationValidationRequestDto.isApplicationApproved()){
              application.setState(ApplicationState.APPROVED);
             notificationInterface.sendNotification(enterprise, msg);
        }

        application.setState(ApplicationState.REJECTED);
        notificationInterface.sendNotification(enterprise, msg);

        return  ResponseEntity.ok().body(msg);

    }

    @DeleteMapping("deleteEnterpriseAccount")
    public ResponseEntity<String> delete(){

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Enterprise enterprise = postOffer.getByEnterpriseEmail(email);
        postOffer.deleteUser(enterprise.getId());

        return ResponseEntity.ok("enterprise deleted successfully");

    }
}
