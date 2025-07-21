package com.internship.management.interfaces;

import com.internship.management.entities.Enterprise;
import com.internship.management.entities.Offer;
import com.internship.management.entities.Teacher;
import com.internship.management.enums.OfferStatus;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface PostOffer {

    Offer getOfferById(Long id);
    Teacher getTeacherById(Long id);
    Offer saveOffer(Offer offer);

    Teacher getTeacherByEmail(String email);
    List<Offer> getOfferByDepartment(String department, OfferStatus offerStatus);
    Enterprise getByEnterpriseEmail(String email);
}
