package com.internship.management.dto.postOffer;


import com.internship.management.entities.Enterprise;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class OfferResponseDto {

    private Long id;
    private String title;
    private String description;
    private String domain;
    private LocalDate startDate;
    private LocalDate endDate;
    private Enterprise enterprise;
    private ConventionRequestDto convention;
}
