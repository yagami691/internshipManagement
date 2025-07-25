package com.internship.management.repositories;

import com.internship.management.entities.Convention;
import com.internship.management.entities.Enterprise;
import com.internship.management.entities.Offer;
import com.internship.management.entities.Teacher;
import com.internship.management.enums.ConventionState;
import com.internship.management.enums.OfferStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Optional;

public interface OfferRepository extends JpaRepository<Offer,Long> {


     List<Offer> findByDomainAndStatus(String department, OfferStatus status);
     boolean existsByTitle(String title);
     List<Offer> findAllByStatus(OfferStatus status);

     List<Offer> findByEnterprise(Enterprise enterprise);

     List<Offer> findByEnterpriseId(Long enterpriseId);

     @Query("SELECT o FROM Offer o JOIN o.convention c WHERE o.status = :offerStatus AND c.conventionState = :conventionState")
     List<Offer> findOffersByStatusAndConventionState(@Param("offerStatus") OfferStatus offerStatus,
                                                      @Param("conventionState") ConventionState conventionState);
}
