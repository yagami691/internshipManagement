package com.internship.management.dto.postOffer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class OfferRequestDto {

    private String title;
    private String description;
    private String domain;
    private String sectorOfActivity;
    private MultipartFile file;
    private MultipartFile logo;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long durationOfInternship;
}
