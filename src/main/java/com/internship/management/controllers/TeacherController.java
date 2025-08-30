package com.internship.management.controllers;


import com.internship.management.dto.InternshipStatDto;
import com.internship.management.dto.StudentResponseDto;
import com.internship.management.dto.postOffer.EnterpriseResponseDto;
import com.internship.management.dto.postOffer.OfferValidationRequestDto;
import com.internship.management.dto.postOffer.OfferResponseDto;
import com.internship.management.entities.*;
import com.internship.management.enums.ConventionState;
import com.internship.management.enums.OfferStatus;
import com.internship.management.interfaces.ChartInterface;
import com.internship.management.interfaces.DepartmentInternshipStat;
import com.internship.management.interfaces.NotificationInterface;
import com.internship.management.interfaces.PostOffer;
import com.internship.management.mappers.PostOfferMapper;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path ="api/teacher")
@RequiredArgsConstructor
@SecurityRequirement(name = "JWT")
public class TeacherController {

    private final PostOffer postOffer;
    private final PostOfferMapper postOfferMapper;
    private final NotificationInterface notificationInterface;
    private final ChartInterface chartInterface;

    @GetMapping("/offerToReview")
    public ResponseEntity<List<OfferResponseDto>> getOffersToReviewByDepartment(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Teacher teacher = postOffer.getTeacherByEmail(email);
        List<Offer> offers = postOffer.getOfferByDepartmentAndPendingOfferStatusAndInPartnershipTrue(teacher.getDepartment(), OfferStatus.PENDING);

        return ResponseEntity.ok(postOfferMapper.toDtoList(offers));
    }

    @GetMapping("/offersApprovedByTeacher")
    public ResponseEntity<List<OfferResponseDto>> getOffersApprovedByTeacher(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        List<Offer> offers = postOffer.getOffersByStatusApprovedAndTeacherEmail(OfferStatus.APPROVED, email);

        return ResponseEntity.ok(postOfferMapper.toDtoList(offers));
    }

    @PutMapping("/offers/{id}/validate")
    public ResponseEntity<String> validateOfferAndConvention(
            @PathVariable Long id,
            @RequestBody OfferValidationRequestDto offerValidationRequest) {

        Offer offer = postOffer.getOfferById(id);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Teacher teacher = postOffer.getTeacherByEmail(email);

        if (offer.getStatus() != OfferStatus.PENDING) {
            throw new IllegalStateException("Offer already processed.");
        }

        offer.setStatus(offerValidationRequest.isOfferApproved() ? OfferStatus.APPROVED : OfferStatus.REJECTED);
        Convention convention;

        if (offer.getConvention() != null) {
            convention = offer.getConvention();

            if (convention.getConventionState() == ConventionState.PENDING) {
                convention.setConventionState(offerValidationRequest.isConventionApproved()
                        ? ConventionState.APPROVED
                        : ConventionState.REJECTED);

                offer.setValidatedBy(teacher);
                offer.setConvention(convention);
            }
        }

        postOffer.saveOffer(offer);

        Enterprise enterprise = offer.getEnterprise();

        String enterpriseMsg =  "Your offer " + offer.getTitle() + " has been reviewed by the " + offer.getValidatedBy().getName() + " teacher.";
        notificationInterface.sendNotification(enterprise, enterpriseMsg);

        if(offer.getStatus() == OfferStatus.APPROVED && offer.getConvention().getConventionState() == ConventionState.APPROVED){
            String studentMsg = "New offer approved by teacher: " + offer.getValidatedBy().getName();

            List<Student> studentsInDepartment = postOffer.getStudentsByDepartment(teacher.getDepartment());

            for (Student s : studentsInDepartment) {
                notificationInterface.sendNotification(s, studentMsg);
            }

        }else{
            enterpriseMsg =  "Your offer \"" + offer.getTitle() + "\" has been rejected by the " + offer.getValidatedBy().getName() + " teacher.";
            notificationInterface.sendNotification(enterprise, enterpriseMsg);
        }

        return ResponseEntity.ok("Offer: " + offer.getStatus()
                + ", Convention: "
                + (offer.getConvention() != null ? offer.getConvention().getConventionState() : "None"));
    }

    @GetMapping("/internshipsByDepartment")
    public List<InternshipStatDto> getInternshipStats() {

        List<DepartmentInternshipStat> stats = chartInterface.getInternshipsByDepartment();

        return stats.stream()
                .map(stat -> new InternshipStatDto(stat.getDepartment(), stat.getCount()))
                .toList();
    }

    @GetMapping("/listOfStudentByDepartment")
    public List<StudentResponseDto> getStudentByDepartment(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Teacher teacher = postOffer.getTeacherByEmail(email);

        List<Student> students = postOffer.getStudentsByDepartment(teacher.getDepartment());
        return postOfferMapper.toDtoStudentList(students);
    }

    @GetMapping("/enterpriseInPartnership")
    public List<EnterpriseResponseDto> getEnterpriseInPartnership(){

        List<Enterprise> listOfEnterpriseInPartnership = postOffer.getEnterpriseByPartnershipTrue();
        return postOfferMapper.toDtoEnterpriseList(listOfEnterpriseInPartnership);
    }
}