package com.internship.management.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor @AllArgsConstructor
public class TeacherRequestDto {

    private String name;
    private String firstName;
    private String email;
    private String password;
    private String department;
}
