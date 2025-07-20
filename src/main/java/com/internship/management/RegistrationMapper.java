package com.internship.management;

import com.internship.management.dto.*;
import com.internship.management.dto.registration.EnterpriseRegistrationRequestDto;
import com.internship.management.dto.registration.StudentRegistrationRequestDto;
import com.internship.management.dto.registration.TeacherRegistrationRequestDto;
import com.internship.management.entities.*;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.time.LocalDateTime;


@Mapper(componentModel = "spring", imports = {LocalDateTime.class})
public interface RegistrationMapper {

    UserResponseDto toDto(Enterprise enterprise);

    UserResponseDto toDto(Student student);

    UserResponseDto toDto(Teacher teacher);

    @Mapping(target = "role", expression = "java(com.internship.management.enums.Role.STUDENT)")
    @Mapping(target = "password", expression = "java(passwordEncoder.encode(studentRequestDto.getPassword()))")
    Student toEntity(StudentRegistrationRequestDto studentRequestDto, @Context PasswordEncoder passwordEncoder);



    @Mapping(target = "role", expression = "java(com.internship.management.enums.Role.ENTERPRISE)")
    @Mapping(target = "password", expression = "java(passwordEncoder.encode(enterpriseRequestDto.getPassword()))")
    Enterprise toEntity(EnterpriseRegistrationRequestDto enterpriseRequestDto, @Context PasswordEncoder passwordEncoder);

    @Mapping(target = "role", expression = "java(com.internship.management.enums.Role.TEACHER)")
    @Mapping(target = "password", expression = "java(passwordEncoder.encode(teacherRequestDto.getPassword()))")
    Teacher toEntity(TeacherRegistrationRequestDto teacherRequestDto, @Context PasswordEncoder passwordEncoder);


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
