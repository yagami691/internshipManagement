package com.internship.management;

import com.internship.management.dto.*;
import com.internship.management.dto.registration.EnterpriseRegistrationRequestDto;
import com.internship.management.dto.registration.StudentRegistrationRequestDto;
import com.internship.management.dto.registration.TeacherRegistrationRequestDto;
import com.internship.management.entities.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.time.LocalDateTime;


@Mapper(componentModel = "spring", imports = {LocalDateTime.class})
public interface RegistrationMapper {

    UserResponseDto toDto(Enterprise enterprise);

    UserResponseDto toDto(Student student);

    UserResponseDto toDto(Teacher teacher);

    @Mapping(target = "role", expression = "java(Role.STUDENT)")
    Student toEntity(StudentRegistrationRequestDto studentRequestDto);


    @Mapping(target = "role", expression = "java(Role.ENTERPRISE)")
    Enterprise toEntity(EnterpriseRegistrationRequestDto enterpriseRequestDto);

    @Mapping(target = "role", expression = "java(Role.TEACHER)")
    Teacher toEntity(TeacherRegistrationRequestDto teacherRequestDto);


/*
    List<UserResponseDto> toDtoList(List<Users> users);
    Offer updateOffer(OfferRequestDto offer);
*/

    int EXPIRATION_MINUTES = 10;

    @Mapping(target = "code", source = "code")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "expirationDate", expression = "java(LocalDateTime.now().plusMinutes(EXPIRATION_MINUTES))")
    VerificationToken verificationTokenUpdate(String code, Users user);


    @Mapping(target = "code", source = "newCode")
    @Mapping(target = "expirationDate", expression = "java(LocalDateTime.now().plusMinutes(EXPIRATION_MINUTES))")
    VerificationToken updateToken(@MappingTarget VerificationToken token, String newCode);


}
