package com.internship.management.controllers;


import com.internship.management.dto.postOffer.OfferRequestDto;
import com.internship.management.dto.postOffer.OfferValidationRequestDto;
import com.internship.management.dto.postOffer.OfferResponseDto;
import com.internship.management.entities.Convention;
import com.internship.management.entities.Offer;
import com.internship.management.entities.Student;
import com.internship.management.entities.Teacher;
import com.internship.management.enums.ConventionState;
import com.internship.management.enums.OfferStatus;
import com.internship.management.interfaces.PostOffer;
import com.internship.management.mappers.PostOfferMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path ="api/teacher")
@RequiredArgsConstructor
public class TeacherController {

    private final PostOffer postOffer;
    private final PostOfferMapper postOfferMapper;

    @GetMapping("/offerToReview")
    public ResponseEntity<List<OfferResponseDto>> getOffersToReviewByDepartment(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Teacher teacher = postOffer.getTeacherByEmail(email);

        List<Offer> offers = postOffer.getOfferByDepartment(teacher.getDepartment(), OfferStatus.PENDING);

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

        if (offer.getConvention() != null) {
            Convention convention = offer.getConvention();

            if (convention.getConventionState() == ConventionState.PENDING) {
                convention.setConventionState(offerValidationRequest.isConventionApproved()
                        ? ConventionState.APPROVED
                        : ConventionState.REJECTED);

                offer.setValidatedBy(teacher);
                offer.setConvention(convention);
                postOffer.saveOffer(offer);
            }
        }

       postOffer.saveOffer(offer);

        return ResponseEntity.ok("Offer: " + offer.getStatus()
                + ", Convention: "
                + (offer.getConvention() != null ? offer.getConvention().getConventionState() : "None"));
    }


    @GetMapping("/convention/{id}/download")
    public ResponseEntity<byte[]> downloadConvention(@PathVariable Long id) {
        Convention convention = postOffer.getConventionById(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=convention.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(convention.getPdfConvention());
    }



    @DeleteMapping("/deleteTeacherAccount")
    public ResponseEntity<String> delete(){

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Teacher teacher = postOffer.getTeacherByEmail(email);
        postOffer.deleteUser(teacher.getId());

        return ResponseEntity.ok("Teacher deleted successfully");

    }




}
