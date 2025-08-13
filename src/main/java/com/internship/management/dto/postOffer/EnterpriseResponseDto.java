package com.internship.management.dto.postOffer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class EnterpriseResponseDto {

    private Long id;
    private String name;
    private String email;
    private String sectorOfActivity;
    private boolean inPartnership;
    private String matriculation;
    private HasLogoDto hasLogo;
}
