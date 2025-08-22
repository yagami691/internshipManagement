package com.internship.management.interfaces;

import com.internship.management.entities.*;
import com.internship.management.enums.ConventionState;
import com.internship.management.enums.OfferStatus;

import java.util.List;

public interface PostOffer {

    Offer getOfferById(Long id);

    Offer saveOffer(Offer offer);

    Teacher getTeacherByEmail(String email);

    Enterprise getByEnterpriseEmail(String email);

    List<Offer> getOffersByStatusAndConventionApproved(OfferStatus offerStatus, ConventionState conventionState, String domain);

    Student getStudentByEmail(String email);

    void saveApplication(Application application);

    List<Application> getAllApplicationsByEnterpriseId(Long id);

    Enterprise getByEnterpriseId(Long id);

    void deleteUser(Long id);

    List<Offer> getOfferByEnterpriseId(Long enterpriseId);

    Users getUserByEmail(String email);

    void saveUser(Users user);

    Application getApplicationById(Long id);

    List<Student> getStudentsByDepartment(String department);

    List<Application> getByApprovedOrRejectedApplication(Long id);

    List<Offer> getOfferPaying(boolean paying);

    List<Offer> getOfferRemote(boolean remote);

    List<Offer> getOfferByPayingAndRemote(boolean paying, boolean remote);

    Logo getLogoByEnterprise(Enterprise enterprise);

    Convention getConventionByOfferId(Long offerId);

    List<Enterprise> getEnterpriseByPartnership();

    List<Offer> getOfferByDepartmentAndPendingOfferStatusAndInPartnershipTrue(String department, OfferStatus offerStatus);

    void saveConvention(Convention convention);
}
