package com.internship.management.controllers;

import com.internship.management.dto.application.ApplicationRequestDto;
import com.internship.management.dto.application.ApplicationResponseDto;
import com.internship.management.dto.application.NotificationDto;
import com.internship.management.dto.offerFiltered.OfferFilterByLocation;
import com.internship.management.dto.offerFiltered.OfferFilterByTime;
import com.internship.management.dto.postOffer.OfferResponseDto;
import com.internship.management.entities.*;
import com.internship.management.enums.ConventionState;
import com.internship.management.enums.OfferStatus;
import com.internship.management.interfaces.NotificationInterface;
import com.internship.management.interfaces.PostOffer;
import com.internship.management.mappers.PostOfferMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/student")
@RequiredArgsConstructor
public class StudentController {

    private final PostOffer postOffer;
    private final PostOfferMapper postOfferMapper;
    private final NotificationInterface notificationInterface;

    @GetMapping("/offersByApprovedStatus")
    public List<OfferResponseDto> getOfferByStatus(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Student student = postOffer.getStudentByEmail(email);

        List<Offer> offers = postOffer.getOffersByStatusAndConventionApproved(OfferStatus.APPROVED, ConventionState.APPROVED, student.getDepartment());
        return postOfferMapper.toDtoList(offers);
    }
//
//    @GetMapping("/filter")
//    public List<OfferResponseDto> filter(@RequestParam(required = false) Long time,
//                                         @RequestParam(required = false) String location) {
//
//        if (time != null && (location == null || location.isEmpty())) {
//            List<Offer> offersByDuration = postOffer.getOfferByDurationOfInternship(time);
//            return postOfferMapper.toDtoList(offersByDuration);
//        }
//
//        if (location != null && !location.isEmpty() && time == null) {
//            List<Offer> offersByLocation = postOffer.getOfferByEnterpriseLocation(location);
//            return postOfferMapper.toDtoList(offersByLocation);
//        }
//
//        return List.of();
//    }

    @GetMapping("/teacherNotifications")
    public ResponseEntity<List<NotificationDto>> getUnseenNotifications() {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Student student = postOffer.getStudentByEmail(email);

        List<Notification> unseen = notificationInterface.getAllUnSeenNotificationsByUser(student);

        return ResponseEntity.ok(
                unseen.stream()
                        .map(n -> new NotificationDto(n.getId(), n.getMessage(), n.getCreatedAt()))
                        .toList()
        );
    }




    @PostMapping("{offer_id}/createApplication")
    public ApplicationResponseDto create(@ModelAttribute ApplicationRequestDto applicationRequestDto, @PathVariable Long offer_id){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Application application = postOfferMapper.toEntity(applicationRequestDto);
        Student student = postOffer.getStudentByEmail(email);
        application.setStudent(student);

        Offer offer = postOffer.getOfferById(offer_id);
        Enterprise enterprise = offer.getEnterprise();
        application.setEnterprise(enterprise);
        application.setOffer(offer);

        postOffer.saveApplication(application);

        String enterpriseMsg = "New application received for the offer: " + offer.getTitle();
        notificationInterface.sendNotification(enterprise, enterpriseMsg);

        return postOfferMapper.toDto(application);
    }

//
//    @PutMapping("/{id}/updateApplication")
//    public ResponseEntity<String> updateApplication(@PathVariable Long id,
//                                                    @ModelAttribute ApplicationRequestDto applicationRequestDto) {
//
//                 Application application = postOffer.getApplicationById(id);
//                 postOfferMapper.updateApplication(application, applicationRequestDto);
//                 postOffer.saveApplication(application);
//
//                 return ResponseEntity.ok("Application updated successfully");
//    }

    @DeleteMapping("/deleteStudentAccount")
    public ResponseEntity<String> delete(){

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Student Student = postOffer.getStudentByEmail(email);
        postOffer.deleteUser(Student.getId());

        return ResponseEntity.ok("Student deleted successfully");

    }
}

