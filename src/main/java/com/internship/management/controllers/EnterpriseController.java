package com.internship.management.controllers;


import com.internship.management.dto.application.ApplicationResponseDto;
import com.internship.management.dto.application.NotificationDto;
import com.internship.management.dto.postOffer.OfferRequestDto;
import com.internship.management.dto.postOffer.OfferResponseDto;
import com.internship.management.entities.*;
import com.internship.management.enums.ApplicationState;
import com.internship.management.interfaces.NotificationInterface;
import com.internship.management.interfaces.PostOffer;
import com.internship.management.mappers.PostOfferMapper;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "api/enterprise")
@RequiredArgsConstructor
@SecurityRequirement(name = "JWT")
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

        if (offerRequestDto.getPdfConvention() != null && !offerRequestDto.getPdfConvention().isEmpty()) {
            Convention c = new Convention();
            c.setPdfConvention(offerRequestDto.getPdfConvention().getBytes());
            c.setOffer(offer);
            offer.setConvention(c);
        }

        postOffer.saveOffer(offer);
        OfferResponseDto offerResponseDto = postOfferMapper.toDto(offer);

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
            msg = "Your application has been " + application.getState() + " and reviewed by the " +
                    enterprise.getName() + " company";
            notificationInterface.sendNotification(student, msg);
            log.info("Application has been {} ",  application.getState());
        }else{

            application.setState(ApplicationState.REJECTED);
            msg = "Your application has been " + application.getState() + " and reviewed by the " +
                    enterprise.getName() + " company";
            notificationInterface.sendNotification(student, msg);
        }

        return  ResponseEntity.ok().body(msg);
    }
}