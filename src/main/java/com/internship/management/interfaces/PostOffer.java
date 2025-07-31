package com.internship.management.interfaces;

import com.internship.management.entities.*;
import com.internship.management.enums.ConventionState;
import com.internship.management.enums.OfferStatus;

import java.util.List;

public interface PostOffer {

    Offer getOfferById(Long id);

    Offer saveOffer(Offer offer);

    Teacher getTeacherByEmail(String email);

    List<Offer> getOfferByDepartment(String department, OfferStatus offerStatus);

    Enterprise getByEnterpriseEmail(String email);

    Convention getConventionById(Long id);

    List<Offer> getOffersByStatusAndConventionApproved(OfferStatus offerStatus, ConventionState conventionState, String domain);

    Student getStudentByEmail(String email);

    void saveApplication(Application application);

    List<Application> getAllApplicationsByEnterpriseId(Long id);

    void deleteUser(Long id);

    List<Offer> getOfferByDurationOfInternship(Long durationOfInternship);

    List<Offer> getOfferByEnterpriseLocation(String location);

    List<Offer> getOfferByEnterpriseId(Long enterpriseId);

    Users getUserByEmail(String email);

    void saveUser(Users user);
}
