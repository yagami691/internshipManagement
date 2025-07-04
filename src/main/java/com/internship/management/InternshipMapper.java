package com.internship.management;

import com.internship.management.dto.*;
import com.internship.management.entities.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InternshipMapper {

    UserResponseDto toDto(Enterprise enterprise);

    UserResponseDto toDto(Student student);

    UserResponseDto toDto(Teacher teacher);

    @Mapping(target = "role", expression = "java(Role.STUDENT)")
    Student toEntity(StudentRequestDto studentRequestDto);

    @Mapping(target = "name", source = "name")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "password", source = "password")
    @Mapping(target = "role", expression = "java(Role.ENTERPRISE)")
    Enterprise toEntity(EnterpriseRequestDto enterpriseRequestDto);

    @Mapping(target = "role", expression = "java(Role.TEACHER)")
    Teacher toEntity(TeacherRequestDto teacherRequestDto);


    List<UserResponseDto> toDtoList(List<Users> users);

    Offer updateOffer(OfferRequestDto offer);

}
