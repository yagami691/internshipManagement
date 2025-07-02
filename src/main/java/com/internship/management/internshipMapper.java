package com.internship.management;

import com.internship.management.dto.EnterpriseRequestDto;
import com.internship.management.dto.StudentRequestDto;
import com.internship.management.dto.UserResponseDto;
import com.internship.management.entities.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface internshipMapper {

    UserResponseDto toDto(User user);
    User toEntity(StudentRequestDto studentRequestDto);
    User toEntity(EnterpriseRequestDto studentRequestDto);
    List<UserResponseDto> toDtoList(List<User> users);

}
