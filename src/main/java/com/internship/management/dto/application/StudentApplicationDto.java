package com.internship.management.dto.application;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class StudentApplicationDto {

    private String name;
    private String firstName;
    private String email;
}
