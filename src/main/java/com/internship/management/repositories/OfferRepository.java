package com.internship.management.repositories;

import com.internship.management.entities.Offer;
import com.internship.management.entities.Teacher;
import com.internship.management.enums.OfferStatus;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;

public interface OfferRepository extends JpaRepository<Offer,Long> {
     Optional<Offer> findByValidatedByAndStatus(Teacher teacher, OfferStatus status);
     List<Offer> findByDomainAndStatus(String department, OfferStatus status);

    boolean existsByTitle(String title);
}
