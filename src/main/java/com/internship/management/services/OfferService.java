package com.internship.management.services;


import com.internship.management.entities.Convention;
import com.internship.management.entities.Enterprise;
import com.internship.management.enums.OfferStatus;
import com.internship.management.interfaces.PostOffer;
import com.internship.management.entities.Offer;
import com.internship.management.entities.Teacher;
import com.internship.management.repositories.ConventionRepository;
import com.internship.management.repositories.EnterpriseRepository;
import com.internship.management.repositories.OfferRepository;
import com.internship.management.repositories.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OfferService implements PostOffer {

    private final OfferRepository offerRepository;
    private final TeacherRepository teacherRepository;
    private final ConventionRepository conventionRepository;
    private final EnterpriseRepository enterpriseRepository;

    public Offer getOfferById(Long id){
        return offerRepository.findById(id)
                .orElseThrow(()->  new RuntimeException("Offer Not Found"));
    }

    public Teacher getTeacherById(Long id){
        return  teacherRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Teacher Not Found"));
    }

    public Convention addConvention(Convention convention){

        boolean isExist = conventionRepository.existsByOffer(convention.getOffer());
        if(isExist){
            throw new RuntimeException("Convention already exists");
        }
        return conventionRepository.save(convention);
    }

    public Offer saveOffer(Offer offer){

        boolean isExist = offerRepository.existsByTitle(offer.getTitle());
        if(isExist){
            throw new RuntimeException("Offer already exists");
        }
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
}
