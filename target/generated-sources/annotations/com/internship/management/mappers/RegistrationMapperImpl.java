package com.internship.management.mappers;

import com.internship.management.dto.UserResponseDto;
import com.internship.management.dto.registration.EnterpriseRegistrationRequestDto;
import com.internship.management.dto.registration.StudentRegistrationRequestDto;
import com.internship.management.dto.registration.TeacherRegistrationRequestDto;
import com.internship.management.entities.Enterprise;
import com.internship.management.entities.Logo;
import com.internship.management.entities.Student;
import com.internship.management.entities.Teacher;
import com.internship.management.entities.Users;
import com.internship.management.entities.VerificationToken;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-29T10:16:03+0100",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.43.0.v20250819-1513, environment: Java 21.0.8 (Eclipse Adoptium)"
)
@Component
public class RegistrationMapperImpl implements RegistrationMapper {

    @Override
    public UserResponseDto toDto(Enterprise enterprise) {
        if ( enterprise == null ) {
            return null;
        }

        UserResponseDto userResponseDto = new UserResponseDto();

        userResponseDto.setEmail( enterprise.getEmail() );
        userResponseDto.setEmailVerified( enterprise.isEmailVerified() );
        userResponseDto.setId( enterprise.getId() );
        userResponseDto.setName( enterprise.getName() );
        if ( enterprise.getRole() != null ) {
            userResponseDto.setRole( enterprise.getRole().name() );
        }

        return userResponseDto;
    }

    @Override
    public UserResponseDto toDto(Student student) {
        if ( student == null ) {
            return null;
        }

        UserResponseDto userResponseDto = new UserResponseDto();

        userResponseDto.setEmail( student.getEmail() );
        userResponseDto.setEmailVerified( student.isEmailVerified() );
        userResponseDto.setId( student.getId() );
        userResponseDto.setName( student.getName() );
        if ( student.getRole() != null ) {
            userResponseDto.setRole( student.getRole().name() );
        }

        return userResponseDto;
    }

    @Override
    public UserResponseDto toDto(Teacher teacher) {
        if ( teacher == null ) {
            return null;
        }

        UserResponseDto userResponseDto = new UserResponseDto();

        userResponseDto.setEmail( teacher.getEmail() );
        userResponseDto.setEmailVerified( teacher.isEmailVerified() );
        userResponseDto.setId( teacher.getId() );
        userResponseDto.setName( teacher.getName() );
        if ( teacher.getRole() != null ) {
            userResponseDto.setRole( teacher.getRole().name() );
        }

        return userResponseDto;
    }

    @Override
    public UserResponseDto toDto(Users user) {
        if ( user == null ) {
            return null;
        }

        UserResponseDto userResponseDto = new UserResponseDto();

        userResponseDto.setEmail( user.getEmail() );
        userResponseDto.setEmailVerified( user.isEmailVerified() );
        userResponseDto.setId( user.getId() );
        userResponseDto.setName( user.getName() );
        if ( user.getRole() != null ) {
            userResponseDto.setRole( user.getRole().name() );
        }

        return userResponseDto;
    }

    @Override
    public Student toEntity(StudentRegistrationRequestDto studentRequestDto, PasswordEncoder passwordEncoder) {
        if ( studentRequestDto == null ) {
            return null;
        }

        Student student = new Student();

        student.setEmail( studentRequestDto.getEmail() );
        student.setName( studentRequestDto.getName() );
        student.setDepartment( studentRequestDto.getDepartment() );
        student.setFirstName( studentRequestDto.getFirstName() );
        student.setGithubLink( studentRequestDto.getGithubLink() );
        List<String> list = studentRequestDto.getLanguages();
        if ( list != null ) {
            student.setLanguages( new ArrayList<String>( list ) );
        }
        student.setLinkedinLink( studentRequestDto.getLinkedinLink() );
        student.setSector( studentRequestDto.getSector() );

        student.setRole( com.internship.management.enums.Role.STUDENT );
        student.setPassword( passwordEncoder.encode(studentRequestDto.getPassword()) );

        return student;
    }

    @Override
    public Enterprise toEntity(EnterpriseRegistrationRequestDto enterpriseRequestDto, PasswordEncoder passwordEncoder) {
        if ( enterpriseRequestDto == null ) {
            return null;
        }

        Enterprise enterprise = new Enterprise();

        enterprise.setEmail( enterpriseRequestDto.getEmail() );
        enterprise.setName( enterpriseRequestDto.getName() );
        enterprise.setCity( enterpriseRequestDto.getCity() );
        enterprise.setContact( enterpriseRequestDto.getContact() );
        enterprise.setCountry( enterpriseRequestDto.getCountry() );
        enterprise.setLocation( enterpriseRequestDto.getLocation() );
        enterprise.setLogo( multipartFileToLogo( enterpriseRequestDto.getLogo(), passwordEncoder ) );
        enterprise.setMatriculation( enterpriseRequestDto.getMatriculation() );
        enterprise.setSectorOfActivity( enterpriseRequestDto.getSectorOfActivity() );

        enterprise.setRole( com.internship.management.enums.Role.ENTERPRISE );
        enterprise.setPassword( passwordEncoder.encode(enterpriseRequestDto.getPassword()) );

        return enterprise;
    }

    @Override
    public Teacher toEntity(TeacherRegistrationRequestDto teacherRequestDto, PasswordEncoder passwordEncoder) {
        if ( teacherRequestDto == null ) {
            return null;
        }

        Teacher teacher = new Teacher();

        teacher.setEmail( teacherRequestDto.getEmail() );
        teacher.setName( teacherRequestDto.getName() );
        teacher.setDepartment( teacherRequestDto.getDepartment() );
        teacher.setFirstName( teacherRequestDto.getFirstName() );

        teacher.setRole( com.internship.management.enums.Role.TEACHER );
        teacher.setPassword( passwordEncoder.encode(teacherRequestDto.getPassword()) );

        return teacher;
    }

    @Override
    public VerificationToken verificationTokenUpdate(String code, Users user) {
        if ( code == null && user == null ) {
            return null;
        }

        VerificationToken verificationToken = new VerificationToken();

        if ( user != null ) {
            verificationToken.setUser( user );
            verificationToken.setId( user.getId() );
        }
        verificationToken.setCode( code );
        verificationToken.setExpirationDate( LocalDateTime.now().plusMinutes(EXPIRATION_MINUTES) );

        return verificationToken;
    }

    @Override
    public VerificationToken updateToken(VerificationToken token, String newCode) {
        if ( newCode == null ) {
            return token;
        }

        token.setCode( newCode );

        token.setExpirationDate( LocalDateTime.now().plusMinutes(EXPIRATION_MINUTES) );

        return token;
    }

    protected Logo multipartFileToLogo(MultipartFile multipartFile, PasswordEncoder passwordEncoder) {
        if ( multipartFile == null ) {
            return null;
        }

        Logo logo = new Logo();

        logo.setContentType( multipartFile.getContentType() );

        return logo;
    }
}
