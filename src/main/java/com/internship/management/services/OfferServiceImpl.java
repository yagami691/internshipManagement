package com.internship.management.services;


import com.internship.management.entities.*;
import com.internship.management.enums.ApplicationState;
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
    private final LogoRepository logoRepository;

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

    public Enterprise getByEnterpriseId(Long id){
        return enterpriseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Enterprise Not Found"));
    }

    public List<Offer> getOffersByStatusAndConventionApproved(OfferStatus offerStatus, ConventionState conventionState, String domain){
        return offerRepository.findOffersByStatusAndConventionStateAndDomain(offerStatus, conventionState, domain);
    }

    public Student getStudentByEmail(String email){
        return studentRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Student Not Found"));
    }

    public List<Student> getStudentsByDepartment(String department){
        return studentRepository.findByDepartment(department);
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

    public List<Application> getByApprovedOrRejectedApplication(Long id){
        return applicationRepository.findApprovedOrRejectedApplicationsByStudentId(id);
    }

    public void deleteUser(Long id){
        userRepository.deleteById(id);
    }

    public List<Offer> getOfferByEnterpriseId(Long enterpriseId){
        return offerRepository.findOfferByEnterpriseId(enterpriseId);
    }

    public Users getUserByEmail(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void saveUser(Users user){
        userRepository.save(user);
    }

    public List<Offer> getOfferPaying(boolean paying){
        return offerRepository.findOffersByEnterprisePaying(paying);
    }

    public List<Offer> getOfferRemote(boolean remote){
        return offerRepository.findOffersByEnterpriseRemote(remote);
    }

    public List<Offer> getOfferByPayingAndRemote(boolean paying, boolean remote){
        return offerRepository.findByRemoteAndPaying(paying, remote);
    }

    public Logo getLogoByEnterprise(Enterprise enterprise){
        return logoRepository.findByEnterprise(enterprise)
                .orElseThrow(() -> new RuntimeException("Logo not found"));
    }

}
