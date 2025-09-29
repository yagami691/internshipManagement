package com.internship.management.mappers;

import com.internship.management.dto.StudentResponseDto;
import com.internship.management.dto.TeacherResponseDto;
import com.internship.management.dto.application.ApplicationOfferDto;
import com.internship.management.dto.application.ApplicationRequestDto;
import com.internship.management.dto.application.ApplicationResponseDto;
import com.internship.management.dto.postOffer.EnterpriseResponseDto;
import com.internship.management.dto.postOffer.OfferRequestDto;
import com.internship.management.dto.postOffer.OfferResponseDto;
import com.internship.management.entities.Application;
import com.internship.management.entities.Enterprise;
import com.internship.management.entities.Offer;
import com.internship.management.entities.Student;
import com.internship.management.entities.Teacher;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-29T10:39:02+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class PostOfferMapperImpl implements PostOfferMapper {

    @Override
    public Offer toEntity(OfferRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        Offer offer = new Offer();

        offer.setTitle( dto.getTitle() );
        offer.setDescription( dto.getDescription() );
        offer.setDomain( dto.getDomain() );
        offer.setJob( dto.getJob() );
        offer.setTypeOfInternship( dto.getTypeOfInternship() );
        offer.setStartDate( dto.getStartDate() );
        offer.setEndDate( dto.getEndDate() );
        offer.setNumberOfPlaces( dto.getNumberOfPlaces() );
        offer.setRequirements( dto.getRequirements() );
        offer.setRemote( dto.isRemote() );
        offer.setPaying( dto.isPaying() );

        return offer;
    }

    @Override
    public OfferResponseDto toDto(Offer offer) {
        if ( offer == null ) {
            return null;
        }

        OfferResponseDto offerResponseDto = new OfferResponseDto();

        offerResponseDto.setConvention( mapConvention( offer.getConvention() ) );
        offerResponseDto.setEnterprise( mapEnterpriseOffer( offer.getEnterprise() ) );
        offerResponseDto.setId( offer.getId() );
        offerResponseDto.setTitle( offer.getTitle() );
        offerResponseDto.setDescription( offer.getDescription() );
        offerResponseDto.setDomain( offer.getDomain() );
        offerResponseDto.setJob( offer.getJob() );
        offerResponseDto.setTypeOfInternship( offer.getTypeOfInternship() );
        offerResponseDto.setNumberOfPlaces( offer.getNumberOfPlaces() );
        offerResponseDto.setStartDate( offer.getStartDate() );
        offerResponseDto.setEndDate( offer.getEndDate() );
        offerResponseDto.setRemote( offer.isRemote() );
        offerResponseDto.setPaying( offer.isPaying() );
        offerResponseDto.setStatus( offer.getStatus() );

        return offerResponseDto;
    }

    @Override
    public List<OfferResponseDto> toDtoList(List<Offer> offers) {
        if ( offers == null ) {
            return null;
        }

        List<OfferResponseDto> list = new ArrayList<OfferResponseDto>( offers.size() );
        for ( Offer offer : offers ) {
            list.add( toDto( offer ) );
        }

        return list;
    }

    @Override
    public Application toEntity(ApplicationRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        Application application = new Application();

        application.setCoverLetter( map( dto.getCoverLetter() ) );
        application.setCv( map( dto.getCv() ) );

        return application;
    }

    @Override
    public ApplicationResponseDto toDto(Application application) {
        if ( application == null ) {
            return null;
        }

        ApplicationResponseDto applicationResponseDto = new ApplicationResponseDto();

        applicationResponseDto.setStudent( mapApplicationStudent( application.getStudent() ) );
        applicationResponseDto.setEnterprise( mapApplicationEnterprise( application.getEnterprise() ) );
        applicationResponseDto.setHasFiles( isFileExist( application ) );
        applicationResponseDto.setId( application.getId() );
        applicationResponseDto.setState( application.getState() );
        applicationResponseDto.setOffer( offerToApplicationOfferDto( application.getOffer() ) );

        return applicationResponseDto;
    }

    @Override
    public List<ApplicationResponseDto> toDtoApplicationList(List<Application> applications) {
        if ( applications == null ) {
            return null;
        }

        List<ApplicationResponseDto> list = new ArrayList<ApplicationResponseDto>( applications.size() );
        for ( Application application : applications ) {
            list.add( toDto( application ) );
        }

        return list;
    }

    @Override
    public List<EnterpriseResponseDto> toDtoEnterpriseList(List<Enterprise> enterpriseList) {
        if ( enterpriseList == null ) {
            return null;
        }

        List<EnterpriseResponseDto> list = new ArrayList<EnterpriseResponseDto>( enterpriseList.size() );
        for ( Enterprise enterprise : enterpriseList ) {
            list.add( toDtoEnterprise( enterprise ) );
        }

        return list;
    }

    @Override
    public EnterpriseResponseDto toDtoEnterprise(Enterprise enterprise) {
        if ( enterprise == null ) {
            return null;
        }

        EnterpriseResponseDto enterpriseResponseDto = new EnterpriseResponseDto();

        enterpriseResponseDto.setOffers( mapEnterpriseToMiniOffer( enterprise ) );
        enterpriseResponseDto.setHasLogo( mapEnterpriseToHasLogoDto( enterprise ) );
        enterpriseResponseDto.setId( enterprise.getId() );
        enterpriseResponseDto.setName( enterprise.getName() );
        enterpriseResponseDto.setEmail( enterprise.getEmail() );
        enterpriseResponseDto.setSectorOfActivity( enterprise.getSectorOfActivity() );
        enterpriseResponseDto.setInPartnership( enterprise.isInPartnership() );
        enterpriseResponseDto.setMatriculation( enterprise.getMatriculation() );
        enterpriseResponseDto.setCountry( enterprise.getCountry() );
        enterpriseResponseDto.setCity( enterprise.getCity() );
        enterpriseResponseDto.setContact( enterprise.getContact() );
        enterpriseResponseDto.setLocation( enterprise.getLocation() );

        return enterpriseResponseDto;
    }

    @Override
    public StudentResponseDto toDtoStudent(Student student) {
        if ( student == null ) {
            return null;
        }

        StudentResponseDto studentResponseDto = new StudentResponseDto();

        studentResponseDto.setId( student.getId() );
        studentResponseDto.setName( student.getName() );
        studentResponseDto.setFirstName( student.getFirstName() );
        studentResponseDto.setEmail( student.getEmail() );
        studentResponseDto.setOnInternship( student.isOnInternship() );
        studentResponseDto.setDepartment( student.getDepartment() );
        studentResponseDto.setSector( student.getSector() );
        List<String> list = student.getLanguages();
        if ( list != null ) {
            studentResponseDto.setLanguages( new ArrayList<String>( list ) );
        }
        studentResponseDto.setGithubLink( student.getGithubLink() );
        studentResponseDto.setLinkedinLink( student.getLinkedinLink() );

        return studentResponseDto;
    }

    @Override
    public List<StudentResponseDto> toDtoStudentList(List<Student> studentList) {
        if ( studentList == null ) {
            return null;
        }

        List<StudentResponseDto> list = new ArrayList<StudentResponseDto>( studentList.size() );
        for ( Student student : studentList ) {
            list.add( toDtoStudent( student ) );
        }

        return list;
    }

    @Override
    public List<TeacherResponseDto> toDtoTeacherList(List<Teacher> teacherList) {
        if ( teacherList == null ) {
            return null;
        }

        List<TeacherResponseDto> list = new ArrayList<TeacherResponseDto>( teacherList.size() );
        for ( Teacher teacher : teacherList ) {
            list.add( teacherToTeacherResponseDto( teacher ) );
        }

        return list;
    }

    protected ApplicationOfferDto offerToApplicationOfferDto(Offer offer) {
        if ( offer == null ) {
            return null;
        }

        ApplicationOfferDto applicationOfferDto = new ApplicationOfferDto();

        applicationOfferDto.setTitle( offer.getTitle() );
        applicationOfferDto.setDescription( offer.getDescription() );
        applicationOfferDto.setDomain( offer.getDomain() );
        applicationOfferDto.setStatus( offer.getStatus() );
        applicationOfferDto.setStartDate( offer.getStartDate() );
        applicationOfferDto.setEndDate( offer.getEndDate() );
        applicationOfferDto.setPaying( offer.isPaying() );
        applicationOfferDto.setRemote( offer.isRemote() );

        return applicationOfferDto;
    }

    protected TeacherResponseDto teacherToTeacherResponseDto(Teacher teacher) {
        if ( teacher == null ) {
            return null;
        }

        TeacherResponseDto teacherResponseDto = new TeacherResponseDto();

        teacherResponseDto.setId( teacher.getId() );
        teacherResponseDto.setName( teacher.getName() );
        teacherResponseDto.setFirstName( teacher.getFirstName() );
        teacherResponseDto.setEmail( teacher.getEmail() );
        teacherResponseDto.setDepartment( teacher.getDepartment() );

        return teacherResponseDto;
    }
}
