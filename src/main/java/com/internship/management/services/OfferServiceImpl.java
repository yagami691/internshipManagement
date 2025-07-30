package com.internship.management.services;


import com.internship.management.entities.*;
import com.internship.management.enums.ConventionState;
import com.internship.management.enums.OfferStatus;
import com.internship.management.interfaces.PostOffer;
import com.internship.management.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OfferServiceImpl implements PostOffer {

    private final OfferRepository offerRepository;
    private final TeacherRepository teacherRepository;
    private final ConventionRepository conventionRepository;
    private final EnterpriseRepository enterpriseRepository;
    private final StudentRepository studentRepository;
    private final ApplicationRepository applicationRepository;
    private final UsersRepository userRepository;

    public Offer getOfferById(Long id){
        return offerRepository.findById(id)
                .orElseThrow(()->  new RuntimeException("Offer Not Found"));
    }

    public Convention getConventionById(Long id){
        return conventionRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Convention Not Found"));
    }

    public Offer saveOffer(Offer offer){
        return offerRepository.save(offer);
    }

    public Teacher getTeacherByEmail(String email){
        return teacherRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

    }

    public List<Offer> getOfferByDepartment(String department, OfferStatus offerStatus){
        return offerRepository.findByDomainAndStatus(department, offerStatus);

    }

    public Enterprise getByEnterpriseEmail(String email){
        return enterpriseRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Enterprise Not Found"));
    }

    public List<Offer> getOffersByStatusAndConventionApproved(OfferStatus offerStatus, ConventionState conventionState, String domain){
        return offerRepository.findOffersByStatusAndConventionStateAndDomain(offerStatus, conventionState, domain);
    }

    public Student getStudentByEmail(String email){
        return studentRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Student Not Found"));
    }

    public void saveApplication(Application application){
        applicationRepository.save(application);
    }

    public List<Application> getAllApplicationsByEnterpriseId(Long id){
        return applicationRepository.findAllByEnterpriseId(id);
    }

    public Application getApplicationById(Long id){
        return applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application Not Found"));
    }

    public void deleteUser(Long id){

        userRepository.deleteById(id);
    }

    public List<Offer> getOfferByDurationOfInternship(Long durationOfInternship){
        return offerRepository.findByDurationOfInternship(durationOfInternship);
    }

    public List<Offer> getOfferByEnterpriseLocation(String location){
        return offerRepository.findByEnterpriseLocation(location);
    }

    public Users getUserByEmail(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void saveUser(Users user){
        userRepository.save(user);
    }
}
