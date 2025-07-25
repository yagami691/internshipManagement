package com.internship.management.dto.postOffer;


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
    private EnterpriseOfferResponseDto enterprise;
    private ConventionResponseDto convention;
}
