package com.internship.management.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentResponseDto {

    private Long id;
    private String name;
    private String firstName;
    private String email;
    private boolean onInternship;
    private String department;
}
