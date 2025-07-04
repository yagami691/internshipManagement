package com.internship.management.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.internship.management.entities.Enterprise;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor @AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class OfferRequestDto {

    private String title;
    private String description;
    private String domain;
    private Enterprise enterprise;
    private LocalDate startDate;
    private LocalDate endDate;
}
