package com.internship.management.dto.postOffer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class EnterpriseOfferResponseDto {

    private Long id;
    private String name;
    private String email;
    private String sectorOfActivity;
    private boolean paying;
    private boolean remote;
    private String matriculation;
    private HasLogoDto hasLogo;
}
