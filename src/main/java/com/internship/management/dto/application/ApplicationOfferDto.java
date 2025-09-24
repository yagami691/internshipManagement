package com.internship.management.dto.application;

import com.internship.management.enums.OfferStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ApplicationOfferDto {

    private String title;
    private String description;
    private String domain;
    private OfferStatus status;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean paying;
    private boolean remote;
}
