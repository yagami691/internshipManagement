package com.internship.management.controllers;


import com.internship.management.dto.application.ApplicationResponseDto;
import com.internship.management.dto.postOffer.EnterpriseResponseDto;
import com.internship.management.dto.postOffer.OfferRequestDto;
import com.internship.management.dto.postOffer.OfferResponseDto;
import com.internship.management.dto.profile.UpdateEnterpriseProfile;
import com.internship.management.entities.*;
import com.internship.management.enums.ApplicationState;
import com.internship.management.interfaces.NotificationInterface;
import com.internship.management.interfaces.PostOffer;
import com.internship.management.mappers.PostOfferMapper;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping(path = "api/enterprise")
@RequiredArgsConstructor
@SecurityRequirement(name = "JWT")
public class EnterpriseController {

    private final PostOffer postOffer;
    private final PostOfferMapper postOfferMapper;
    private final NotificationInterface notificationInterface;

    @PostMapping("/createOffer")
    public ResponseEntity<OfferResponseDto> create (@RequestBody OfferRequestDto offerRequestDto){

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Enterprise enterprise = postOffer.getByEnterpriseEmail(email);

        Offer offer = postOfferMapper.toEntity(offerRequestDto);
        offer.setEnterprise(enterprise);

        postOffer.saveOffer(offer);
        return ResponseEntity.ok(postOfferMapper.toDto(offer));
    }

    @PostMapping( "/{offerId}/convention")
    public  ResponseEntity<OfferResponseDto> createPdfConvention (@PathVariable Long offerId, @RequestParam MultipartFile pdfConvention) throws IOException {

        Offer offer = postOffer.getOfferById(offerId);

        if (pdfConvention != null && !pdfConvention.isEmpty()) {

            Convention c = new Convention();
            c.setPdfConvention(pdfConvention.getBytes());
            c.setOffer(offer);
            postOffer.saveConvention(c);
            offer.setConvention(c);
        }

        postOffer.saveOffer(offer);

        List<Teacher> teachers = postOffer.getTeachersByDepartment(offer.getDomain());

        for (Teacher teacher : teachers ) {
            notificationInterface.sendNotification(teacher, "Nouvel arrivage d'offres");
        }

        return ResponseEntity.ok(postOfferMapper.toDto(offer));
    }


    @GetMapping("/Applications")
    public List<ApplicationResponseDto> getApplications() {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Enterprise enterprise = postOffer.getByEnterpriseEmail(email);
        List<Application> applications = postOffer.getAllApplicationsByEnterpriseId(enterprise.getId());

        return postOfferMapper.toDtoApplicationList(applications);
    }

    @GetMapping("/listOfOffers")
    public List<OfferResponseDto> getOffers() {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Enterprise enterprise = postOffer.getByEnterpriseEmail(email);
        List<Offer> offersByEnterpriseId = postOffer.getOfferByEnterpriseId(enterprise.getId());

        return postOfferMapper.toDtoList(offersByEnterpriseId);
    }

    @PutMapping("application/{id}/validate")
    public ResponseEntity<String> validateApplication(@PathVariable Long id,
                                                      @RequestParam boolean approved) {

        Application application = postOffer.getApplicationById(id);

        Student student = application.getStudent();

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Enterprise enterprise = postOffer.getByEnterpriseEmail(email);

        String msg = "";

        if(approved){

            application.setState(ApplicationState.APPROVED);
            msg = "Votre candidature a été approuvée et examiner par l'entreprise " +
                    enterprise.getName();
            notificationInterface.sendNotification(student, msg);

        }else{

            application.setState(ApplicationState.REJECTED);
            msg = "Votre candidature a été rejetée et examiner par l'entreprise " +
                    enterprise.getName();
            notificationInterface.sendNotification(student, msg);
        }

        return  ResponseEntity.ok().body(msg);
    }

    @GetMapping("/info")
    public ResponseEntity<EnterpriseResponseDto> getCurrentEnterpriseInfo() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Enterprise enterprise = postOffer.getByEnterpriseEmail(email);
        return ResponseEntity.ok(postOfferMapper.toDtoEnterprise(enterprise));
    }

    @PatchMapping("/updateContact")
    public ResponseEntity<String> updateContact(@RequestBody UpdateEnterpriseProfile updateEnterpriseProfile) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Enterprise enterprise = postOffer.getByEnterpriseEmail(email);

        enterprise.setContact(updateEnterpriseProfile.getContact());
        postOffer.saveUser(enterprise);

        return ResponseEntity.ok().body(enterprise.getName() + " updated contact successfully");
    }

    @PatchMapping("/updateLocation")
    public ResponseEntity<String> updateLocation(@RequestBody UpdateEnterpriseProfile updateEnterpriseProfile) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Enterprise enterprise = postOffer.getByEnterpriseEmail(email);

        enterprise.setLocation(updateEnterpriseProfile.getLocation());
        postOffer.saveUser(enterprise);

        return ResponseEntity.ok().body(enterprise.getName() + " updated location successfully");
    }

    @PutMapping("/updateLogo/{enterpriseId}")
    public ResponseEntity<String> updateLogo(
            @PathVariable Long enterpriseId,
            @RequestParam("file") MultipartFile file) {

        try {

            postOffer.updateLogo(enterpriseId, file);
            return ResponseEntity.ok("Logo updated successfully");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: " + e.getMessage());
        }
    }
}