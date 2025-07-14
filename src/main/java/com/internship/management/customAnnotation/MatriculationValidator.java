package com.internship.management.customAnnotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;

public class MatriculationValidator implements ConstraintValidator<ValidMatriculation, String> {


    private final List<String> tradeRegister = List.of(
            "RC/DLA/2023/A/04567",
            "RC/DLA/2022/B/12345",
            "RC/DLA/2021/C/99999"
    );

    @Override
    public boolean isValid(String matriculation, ConstraintValidatorContext context) {
        return matriculation != null && tradeRegister.contains(matriculation);
    }
}
