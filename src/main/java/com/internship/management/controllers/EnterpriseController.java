package com.internship.management.controllers;


import com.internship.management.dto.postOffer.OfferRequestDto;
import com.internship.management.dto.postOffer.OfferResponseDto;
import com.internship.management.entities.Enterprise;
import com.internship.management.entities.Offer;
import com.internship.management.interfaces.PostOffer;
import com.internship.management.mappers.PostOfferMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/enterprise")
@RequiredArgsConstructor
public class EnterpriseController {

    private final PostOffer postOffer;
    private final PostOfferMapper postOfferMapper;

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

}
