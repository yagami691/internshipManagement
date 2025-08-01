package com.internship.management.repositories;

import com.internship.management.entities.Offer;
import com.internship.management.enums.ConventionState;
import com.internship.management.enums.OfferStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;

public interface OfferRepository extends JpaRepository<Offer,Long> {

     List<Offer> findByDomainAndStatus(String department, OfferStatus status);

     @Query("SELECT o FROM Offer o JOIN o.convention c " +
             "WHERE o.status = :offerStatus AND c.conventionState = :conventionState AND o.domain = :domain")
     List<Offer> findOffersByStatusAndConventionStateAndDomain(
             @Param("offerStatus") OfferStatus offerStatus,
             @Param("conventionState") ConventionState conventionState,
             @Param("domain") String domain
     );

     List<Offer> findOfferByEnterpriseId(Long enterpriseId);

     @Query("SELECT o FROM Offer o WHERE o.enterprise.paying = :paying")
     List<Offer> findOffersByEnterprisePaying(@Param("paying") boolean paying);

     @Query("SELECT o FROM Offer o WHERE o.enterprise.remote = :remote")
     List<Offer> findOffersByEnterpriseRemote(@Param("remote") boolean remote);

     @Query("SELECT o FROM Offer o WHERE o.enterprise.remote = :remote AND o.enterprise.paying = :paying")
     List<Offer> findByRemoteAndPaying(@Param("remote") boolean remote, @Param("paying") boolean paying);

}
