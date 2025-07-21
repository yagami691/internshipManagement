package com.internship.management.dto.registration;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.internship.management.customAnnotation.ValidMatriculation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor @AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class EnterpriseRegistrationRequestDto {

    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Email is invalid")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Sector is required")
    private String sector;

    @ValidMatriculation
    private String matriculation;

    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;
}
