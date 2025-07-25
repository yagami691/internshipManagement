package com.internship.management.repositories;

import com.internship.management.entities.Application;
import com.internship.management.entities.Offer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application,Long> {

    List<Application> findAllByOfferTitle(String offerTitle);

    List<Application> findAllByOffer(Offer offer);

    List<Application> findAllByEnterpriseId(Long id);
}
