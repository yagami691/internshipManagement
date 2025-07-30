package com.internship.management.controllers;


import com.internship.management.dto.application.ApplicationResponseDto;
import com.internship.management.dto.application.NotificationDto;
import com.internship.management.dto.postOffer.OfferRequestDto;
import com.internship.management.dto.postOffer.OfferResponseDto;
import com.internship.management.entities.Application;
import com.internship.management.entities.Enterprise;
import com.internship.management.entities.Notification;
import com.internship.management.entities.Offer;
import com.internship.management.interfaces.NotificationInterface;
import com.internship.management.interfaces.PostOffer;
import com.internship.management.mappers.PostOfferMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<OfferResponseDto> create (@ModelAttribute OfferRequestDto offerRequestDto) {

           String email = SecurityContextHolder.getContext().getAuthentication().getName();
           Enterprise enterprise = postOffer.getByEnterpriseEmail(email);

           Offer offer = postOfferMapper.toEntity(offerRequestDto);
           offer.setEnterprise(enterprise);
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


    @DeleteMapping("deleteEnterpriseAccount")
    public ResponseEntity<String> delete(){

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Enterprise enterprise = postOffer.getByEnterpriseEmail(email);
        postOffer.deleteUser(enterprise.getId());

        return ResponseEntity.ok("enterprise deleted successfully");

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


}
